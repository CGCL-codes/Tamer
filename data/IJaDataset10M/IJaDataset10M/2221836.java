package cz.cvut.phone.bp.parsers;

import cz.cvut.phone.bp.core.ParserPluginInterface;
import cz.cvut.phone.bp.dto.PaymentDTO;
import cz.cvut.phone.bp.dto.PaymentListDTO;
import cz.cvut.phone.mailer.DTO.MessageDTO;
import cz.cvut.phone.mailer.DTO.MessageListDTO;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Jarda
 */
public class KBFileParser implements ParserPluginInterface {

    private static final String code = "0100";

    private static final String startKB = "Komerční banka a.s.";

    private static final String kuctu = "k účtu:";

    private static final String uhradazjb = "Úhrada z jiné banky";

    private static final String splitter = "____________________________________________________________________________________________________";

    private static final String platbavklad = "Platba/vklad ve prospěch účtu";

    private static final String filterBySender = "info@kb.cz";

    private static final String mailStart = "Oznamujeme Vam provedeni platby";

    private static final String mailZUctu = "z uctu cislo";

    private static final String mailNaUcet = "na ucet cislo";

    private static final String mailCastka = "castka";

    private static final String mailDatum = "datum splatnosti";

    private static final String mailVariable = "variabilni symbol platby";

    private static final String mailSpecific = "specificky symbol";

    public KBFileParser() {
    }

    public PaymentListDTO parsePaymentsFromFile(String filename, String mime, byte[] data) {
        PaymentListDTO pl = new PaymentListDTO();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] text;
        try {
            baos.write(data);
            text = baos.toString().split(splitter);
        } catch (Exception e) {
            pl.setError(10);
            return pl;
        }
        int position;
        if ((position = text[0].indexOf(kuctu)) < 0) {
            pl.setError(20);
            return pl;
        }
        try {
            String account = text[0].substring(position + 10, position + 16).trim();
            for (int i = 1; i < text.length; i++) {
                position = -1;
                int posuhrada = text[i].indexOf(uhradazjb);
                int posvklad = text[i].indexOf(platbavklad);
                if (posuhrada > -1) {
                    position = posuhrada;
                } else if (posvklad > -1) {
                    position = posvklad;
                }
                if (position > -1) {
                    String date = text[i].substring(position - 13, position - 1).trim();
                    String variableSymbol = text[i].substring(position + 33, position + 45).trim();
                    String constantSymbol = text[i].substring(position + 134, position + 146).trim();
                    String specificSymbol = text[i].substring(position + 235, position + 247).trim();
                    String ammount = text[i].substring(position + 67, position + 86).trim();
                    ammount = ammount.replace(".", "");
                    ammount = ammount.replace(",", ".");
                    String accountNumberSender = "";
                    String transactionID = date + variableSymbol + specificSymbol;
                    transactionID += ammount.replace(".", "");
                    String noteForSender = "";
                    String noteForReciever = "";
                    Calendar cal = Calendar.getInstance();
                    cal.clear();
                    String[] dmy = date.split("-");
                    cal.set(Calendar.YEAR, Integer.parseInt(dmy[2]));
                    cal.set(Calendar.MONTH, Integer.parseInt(dmy[1]) - 1);
                    cal.set(Calendar.DATE, Integer.parseInt(dmy[0]));
                    Timestamp time = new Timestamp(cal.getTimeInMillis());
                    pl.addPayment(new PaymentDTO(account, accountNumberSender, specificSymbol, variableSymbol, constantSymbol, Double.parseDouble(ammount), noteForSender, noteForReciever, transactionID, time));
                    pl.setError(0);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            pl.setError(30);
            return pl;
        } catch (RuntimeException e) {
            pl.setError(100);
            return pl;
        }
        return pl;
    }

    public boolean isMyFile(String startText) {
        if (startText.contains(startKB)) {
            return true;
        }
        return false;
    }

    public PaymentListDTO parsePaymentsFromEmail(String pop3, int pop3Port, String pass, String username, String accountNumber) {
        PaymentListDTO pl = new PaymentListDTO();
        MessageListDTO mlist = mailFacade.receiveMessages(filterBySender, "", pop3, pop3Port, username, pass);
        if (mlist.getError() != 0) {
            pl.setError(mlist.getError());
            return pl;
        }
        List<MessageDTO> ml = mlist.getMessages();
        try {
            for (MessageDTO m : ml) {
                String text = m.getText();
                int position = text.indexOf(mailStart);
                if (position >= 0) {
                    position = text.indexOf(mailDatum);
                    String date = text.substring(position + 17, position + 27).trim();
                    position = text.indexOf(mailVariable);
                    String variableSymbol = text.substring(position + 25, position + 36).trim();
                    int helpPosition = variableSymbol.indexOf(",");
                    variableSymbol = variableSymbol.substring(0, helpPosition);
                    position = text.indexOf(mailSpecific);
                    String specificSymbol = text.substring(position + 18, position + 29).trim();
                    helpPosition = specificSymbol.indexOf(".");
                    specificSymbol = specificSymbol.substring(0, helpPosition);
                    position = text.indexOf(mailCastka);
                    String constantSymbol = "";
                    String ammount = text.substring(position + 7, position + 60).trim();
                    helpPosition = ammount.indexOf("CZK");
                    if (helpPosition > 0) {
                        ammount = ammount.substring(0, helpPosition - 1);
                        ammount = ammount.replace(" ", "");
                        ammount = ammount.replace(",", ".");
                    } else {
                        continue;
                    }
                    position = text.indexOf(mailNaUcet);
                    String account = text.substring(position + 14, position + 55);
                    helpPosition = account.indexOf("castka");
                    account = account.substring(0, helpPosition - 1);
                    String accountNumberSender = "";
                    date = date.replace(".", "-");
                    String transactionID = date + variableSymbol + specificSymbol;
                    transactionID += ammount.replace(".", "");
                    String noteForSender = "";
                    String noteForReciever = "";
                    Calendar cal = Calendar.getInstance();
                    cal.clear();
                    String[] dmy = date.split("-");
                    cal.set(Calendar.YEAR, Integer.parseInt(dmy[2]));
                    cal.set(Calendar.MONTH, Integer.parseInt(dmy[1]) - 1);
                    cal.set(Calendar.DATE, Integer.parseInt(dmy[0]));
                    Timestamp time = new Timestamp(cal.getTimeInMillis());
                    pl.addPayment(new PaymentDTO(account, accountNumberSender, specificSymbol, variableSymbol, constantSymbol, Double.parseDouble(ammount), noteForSender, noteForReciever, transactionID, time));
                    pl.setError(0);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            pl.setError(30);
            return pl;
        } catch (RuntimeException e) {
            pl.setError(100);
            return pl;
        }
        return pl;
    }

    public boolean isMyEmail(String codeText) {
        if (codeText.equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }
}

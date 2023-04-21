package tabdulin.sms.megafon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import org.apache.log4j.Logger;
import tabdulin.sms.InvalidSmsException;
import tabdulin.sms.Operator;
import tabdulin.sms.Sms;
import tabdulin.util.FileDownloader;
import tabdulin.util.RandomGenerator;

/**
 * @author Talgat Abdulin
 *
 */
public class MegafonOperator implements Operator {

    private static final Logger logger = Logger.getLogger(MegafonOperator.class);

    private static final int MAX_MESSAGE_LENGTH_LATIN = 110;

    private static final int MAX_MESSAGE_LENGTH_CYRILLIC = 55;

    private static final String WRITE_SMS_URL = "http://sms.megafonmoscow.ru/";

    private static final String CAPTCHA_URL = "http://sms.megafonmoscow.ru/captcha/";

    private static final String SEND_SMS_URL = "http://sms.megafonmoscow.ru/sms.action";

    private HttpURLConnection urlConnection;

    private String captchaID;

    private File captchaFile;

    /**
     * @param urlString
     * @throws IOException
     */
    public MegafonOperator() throws IOException {
    }

    public void startSession() throws MalformedURLException, IOException {
        this.urlConnection = (HttpURLConnection) new URL(WRITE_SMS_URL).openConnection();
        InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
        ParserDelegator parser = new ParserDelegator();
        HTMLEditorKit.ParserCallback cb = new MegafonCaptchaSeeker();
        parser.parse(isr, cb, false);
        isr.close();
        this.captchaID = ((MegafonCaptchaSeeker) cb).getCaptchaID();
        logger.debug("Captcha ID : " + this.captchaID);
        captchaFile = new File("captcha" + RandomGenerator.getInt() + ".jpeg");
        FileDownloader.download(CAPTCHA_URL + this.captchaID, captchaFile.getAbsolutePath());
    }

    public String getCaptchaFileName() {
        return captchaFile.getAbsolutePath();
    }

    public void validate(Sms sms) throws InvalidSmsException {
        if (sms == null) {
            throw new InvalidSmsException(Sms.UNKNOWN_ERROR);
        }
        if (sms.getCode() == null || sms.getMessage() == null || sms.getNumber() == null) {
            throw new InvalidSmsException(Sms.UNKNOWN_ERROR);
        }
        if (sms.getCode().length() != 4) {
            throw new InvalidSmsException(Sms.CAPTCHA_CODE_ERROR);
        }
        if (sms.isTranslitUsed() && sms.getMessage().length() > MAX_MESSAGE_LENGTH_LATIN || !sms.isTranslitUsed() && sms.getMessage().length() > MAX_MESSAGE_LENGTH_CYRILLIC) {
            throw new InvalidSmsException(Sms.TOO_LONG_MESSAGE_ERROR);
        }
        if (sms.getNumber().length() != 7) {
            throw new InvalidSmsException(Sms.NUMBER_ERROR);
        }
        try {
            Integer.parseInt(sms.getNumber());
        } catch (NumberFormatException e) {
            throw new InvalidSmsException(Sms.NUMBER_ERROR);
        }
    }

    public InputStream send(Sms sms) throws IOException {
        urlConnection = (HttpURLConnection) new URL(SEND_SMS_URL).openConnection();
        urlConnection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        StringBuffer query = new StringBuffer();
        if (sms.isTranslitUsed()) {
            query.append("transliterate=checked&");
        }
        query.append("charcheck=йцукен&");
        query.append("lang=rus&");
        query.append("codekey=" + this.captchaID + "&");
        query.append("codevalue=" + sms.getCode() + "&");
        query.append("prefix=" + "7" + sms.getPrefix() + "&");
        query.append("addr=" + sms.getNumber() + "&");
        query.append("message=");
        if (sms.getSender() != null && sms.getSender().trim().length() != 0) {
            query.append(FROM + sms.getSender() + ".");
        }
        query.append(sms.getMessage() + "\r\n");
        logger.debug("POST Query : " + query.toString());
        out.write(query.toString());
        out.flush();
        out.close();
        return urlConnection.getInputStream();
    }

    public int checkStatus() {
        return 0;
    }

    @Override
    public void closeSession() {
        if (captchaFile != null && captchaFile.exists()) {
            logger.debug("Deleting captcha file \"" + captchaFile.getName() + "\"");
            if (captchaFile.delete()) {
                logger.debug("Captcha file was deleted.");
                return;
            }
            logger.error("Captcha " + captchaFile.getName() + "was not deleted!!!");
        }
    }

    public int getMaxMessageLength(boolean translit) {
        if (translit) {
            return MAX_MESSAGE_LENGTH_LATIN;
        }
        return MAX_MESSAGE_LENGTH_CYRILLIC;
    }
}

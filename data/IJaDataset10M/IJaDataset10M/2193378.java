package sisi.movimenti;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zkoss.zul.Listbox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import sisi.MyEmf;
import sisi.nominativi.Nominativi;
import sisi.tipimov.Tipimov;

public class EstrattocontoController {

    public static Estrattoconto[] EstrattoContoEC;

    public static Estrattoconto EstrattoContoSelezionato;

    public Estrattoconto[] getEstrattocontoEC() {
        if (EstrattoContoEC == null) {
            EstrattoContoEC = getEstrattoconto(null, null, 'N', 'S', "1", "", "");
        }
        return EstrattoContoEC;
    }

    public Estrattoconto getEstrattocontoSelezionato() {
        if (EstrattoContoSelezionato == null) {
            EstrattoContoSelezionato = new Estrattoconto();
            azzeraECSelezionato();
        }
        return EstrattoContoSelezionato;
    }

    public void azzeraECSelezionato() {
        if (EstrattoContoSelezionato == null) {
            EstrattoContoSelezionato = new Estrattoconto();
        }
        EstrattoContoSelezionato.setCodtipmov("");
    }

    private EntityManager getEntityManager() {
        return new MyEmf().getEntityManager();
    }

    @SuppressWarnings("unchecked")
    public Estrattoconto[] getEstrattoconto(String cFiltro, String clifor, char dettaglio, char incsaldozero, String ordine, String gruppo, String sottogruppo) {
        boolean lDettaglio = (dettaglio == 'S');
        boolean lIncSaldoZero = (incsaldozero == 'S');
        ordine = (ordine == null || ordine.isEmpty() ? "1" : ordine);
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select mt from Movtesta as mt ";
            cQuery += " JOIN mt.tipomovimento tm ";
            if (cFiltro != null && !cFiltro.isEmpty()) {
                cQuery += " WHERE UPPER(mt.codnom) LIKE '%" + cFiltro.trim().toUpperCase() + "%' ";
            } else {
                if (cliOfor(clifor).equals("C")) {
                    cQuery += " JOIN mt.nominativi nm WHERE nm.nomcli='S' ";
                } else {
                    cQuery += " JOIN mt.nominativi nm WHERE nm.nomfor='S' ";
                }
            }
            cQuery += " AND tm.tmmovec='S'  ";
            if (gruppo != null && !gruppo.isEmpty()) {
                cQuery += " AND mt.gruppo=" + gruppo;
            }
            if (sottogruppo != null && !sottogruppo.isEmpty()) {
                cQuery += " AND mt.subgruppo=" + sottogruppo;
            }
            cQuery += " order by mt.tid";
            javax.persistence.Query q = em.createQuery(cQuery);
            List lista1 = q.getResultList();
            Object[] ar2 = CalcoloEC(lista1, cFiltro, lDettaglio, lIncSaldoZero, ordine);
            EstrattoContoEC = (Estrattoconto[]) ar2;
            return (Estrattoconto[]) ar2;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Estrattoconto[] CalcoloEC(List listaMovTesta, String cFiltro, boolean lDettaglio, boolean lIncSaldoZero, String ordine) {
        BigDecimal nSaldo = new BigDecimal("0");
        BigDecimal nSaldo2 = new BigDecimal("0");
        try {
            Iterator iterator = listaMovTesta.iterator();
            ArrayList<Estrattoconto> myArr = new ArrayList<Estrattoconto>();
            while (iterator.hasNext()) {
                Movtesta mt = (Movtesta) iterator.next();
                Estrattoconto ec = new Estrattoconto();
                ec.setCodnom(mt.getCodnom());
                ec.setTipomovimento(mt.getTipomovimento());
                ec.setNominativo(mt.getNominativi());
                ec.setNummov(mt.getNummov());
                ec.setPartita(mt.getPartita());
                ec.setSaldo((BigDecimal.ZERO));
                ec.setCodtipmov(mt.getCodtipmov());
                ec.setCodiceNominativo(mt.getNominativi().getCodnom());
                ec.setNomeNominativo(mt.getNominativi().getNomnom());
                ec.setCodiceMovimento(mt.getCodtipmov());
                ec.setDescrizioneMovimento(mt.getTipomovimento().getTmdes());
                ec.setNumeroDocumento(mt.getTndoc());
                ec.setDataDocumento(mt.getTddoc());
                ec.setMovimento(mt);
                ec.setIdtesta(mt.getTid());
                boolean lDaAggiungere = true;
                if (ec.getTipomovimento() == null || ec.getTipomovimento().getTmtipmov() == null || ec.getTipomovimento().getTmtipmov().isEmpty()) {
                    lDaAggiungere = false;
                } else if (ec.getTipomovimento().getTmtipmov().equals("5")) {
                    ec.setImportodoc(BigDecimal.ZERO);
                    ec.setImportopag(mt.getTimpon());
                    nSaldo = nSaldo.subtract(ec.getImportopag());
                    ec.setNumeroxOrdinamento('3');
                } else {
                    ec.setImportodoc(mt.getTimpon().add(mt.getTiva()));
                    ec.setImportopag(BigDecimal.ZERO);
                    if (ec.getTipomovimento().getTmtipmov().equals("7")) {
                        ec.setImportodoc(mt.getTimpon().negate().add(mt.getTiva().negate()));
                        if (mt.getNumordine() == null || mt.getNumordine().isEmpty() || mt.getDataordine() == null) {
                        } else {
                            ec.setPartita(mt.getNumordine().trim() + "/" + (mt.getDataordine().getYear() + 1900));
                        }
                        nSaldo = nSaldo.subtract(ec.getImportodoc());
                        ec.setNumeroxOrdinamento('2');
                    } else {
                        nSaldo = nSaldo.add(ec.getImportodoc());
                        ec.setNumeroxOrdinamento('1');
                    }
                }
                if (ec.getPartita() == null) {
                    ec.setPartita("");
                }
                if (lDaAggiungere) {
                    if (!lDettaglio) {
                        myArr.add(ec);
                    }
                    if (lDettaglio) {
                        List riferimenti = mt.getRiferimentimovimento();
                        Iterator iteratorrif = riferimenti.iterator();
                        if (riferimenti.size() <= 0) {
                            myArr.add(ec);
                        } else {
                            while (iteratorrif.hasNext()) {
                                Movrif mr = (Movrif) iteratorrif.next();
                                Estrattoconto ecrif = new Estrattoconto();
                                ecrif.setCodnom(mt.getCodnom());
                                ecrif.setTipomovimento(mt.getTipomovimento());
                                ecrif.setNominativo(mt.getNominativi());
                                ecrif.setCodiceNominativo(mt.getCodnom());
                                ecrif.setNomeNominativo(mt.getNominativi().getNomnom());
                                ecrif.setCodiceMovimento(mt.getCodtipmov());
                                ecrif.setDescrizioneMovimento(mt.getTipomovimento().getTmdes());
                                ecrif.setNumeroDocumento(mt.getTndoc());
                                ecrif.setDataDocumento(mt.getTddoc());
                                ecrif.setPartita(mr.getPartita());
                                ecrif.setImportopag(mr.getImporto());
                                ecrif.setImportodoc(BigDecimal.ZERO);
                                ecrif.setSaldo((BigDecimal.ZERO));
                                ecrif.setNumeroxOrdinamento('3');
                                ecrif.setIdtesta(0);
                                if (ecrif.getPartita() == null) {
                                    ecrif.setPartita("");
                                }
                                if (ecrif != null) {
                                    myArr.add(ecrif);
                                }
                            }
                        }
                    }
                }
            }
            if (lDettaglio) {
                Collections.sort(myArr, PARTITA_ORDER);
                ArrayList<String> partDaCanc = new ArrayList<String>();
                Object[] object_ec_tmp = myArr.toArray(new Estrattoconto[0]);
                Estrattoconto[] array_ec = (Estrattoconto[]) object_ec_tmp;
                int i = 0;
                while (i <= array_ec.length - 1) {
                    nSaldo2 = BigDecimal.ZERO;
                    String cPartita = array_ec[i].getPartita();
                    String codicenominativo = array_ec[i].getCodiceNominativo();
                    String nomeNominativo = array_ec[i].getNomeNominativo();
                    Date datadoc2 = array_ec[i].getDataDocumento();
                    while (i <= array_ec.length - 1) {
                        datadoc2 = array_ec[i].getDataDocumento();
                        BigDecimal impDoc = array_ec[i].getImportodoc();
                        BigDecimal impPag = array_ec[i].getImportopag();
                        impDoc = impDoc.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        impPag = impPag.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        nSaldo2 = nSaldo2.add(impDoc).subtract(impPag);
                        nSaldo2 = nSaldo2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        i++;
                        if (i > array_ec.length - 1 || (!array_ec[i].getPartita().trim().equals(cPartita.trim()) || !array_ec[i].getCodiceNominativo().trim().equals(codicenominativo.trim()))) {
                            break;
                        }
                    }
                    Nominativi tmpnom = new sisi.nominativi.NominativiController().getNominativoXCodice(codicenominativo);
                    Estrattoconto ec2 = new Estrattoconto();
                    ec2.setCodnom("saldo");
                    ec2.setTipomovimento(new Tipimov());
                    ec2.setNominativo(tmpnom);
                    ec2.setNummov(0);
                    ec2.setPartita(cPartita);
                    ec2.setSaldo(nSaldo2);
                    ec2.setCodtipmov("");
                    ec2.setImportodoc(BigDecimal.ZERO);
                    ec2.setImportopag(BigDecimal.ZERO);
                    ec2.setMovimento(new Movtesta());
                    ec2.setCodiceNominativo(codicenominativo);
                    ec2.setNomeNominativo(nomeNominativo);
                    ec2.setCodiceMovimento("");
                    ec2.setDescrizioneMovimento(" * * * SALDO PARTITA * * *");
                    ec2.setNumeroDocumento("");
                    ec2.setDataDocumento(datadoc2);
                    ec2.setNumeroxOrdinamento('4');
                    ec2.setIdtesta(0);
                    if (!lIncSaldoZero && nSaldo2.compareTo(BigDecimal.ZERO) == 0) {
                        partDaCanc.add(codicenominativo + "-" + cPartita);
                    } else {
                        myArr.add(ec2);
                    }
                }
                i = 0;
                while (i <= myArr.size() - 1) {
                    if (myArr.get(i).getPartita() == null) {
                        break;
                    }
                    String cPartita = myArr.get(i).getPartita().trim();
                    String codnom = myArr.get(i).getCodiceNominativo();
                    int nIndex = partDaCanc.indexOf(codnom + "-" + cPartita);
                    if (nIndex >= 0) {
                        myArr.remove(i);
                    } else {
                        i++;
                    }
                }
                if (ordine.equals("1")) {
                    Collections.sort(myArr, PARTITA_ORDER);
                } else if (ordine.equals("2")) {
                    Collections.sort(myArr, PARTITA_NOMINATIVO_ORDER);
                } else if (ordine.equals("3")) {
                    Collections.sort(myArr, PARTITA_CITTA_NOMINATIVO_ORDER);
                }
            } else {
                Collections.sort(myArr, DATADOC_ORDER);
            }
            Object[] arpp = myArr.toArray(new Estrattoconto[0]);
            return (Estrattoconto[]) arpp;
        } finally {
        }
    }

    static String cliOfor(String clifor) {
        if (clifor == null || clifor.isEmpty()) {
            clifor = "C";
        }
        return (clifor.toUpperCase());
    }

    public static String formattapartita(String cPartita) {
        String cTmpP = "";
        char spazio = ' ';
        cTmpP = cPartita.trim();
        cTmpP = sisi.General.paddingString(cTmpP, 13, spazio, false).trim();
        if (cTmpP.lastIndexOf("/") != -1) {
            String c1 = cTmpP.substring(0, cTmpP.lastIndexOf("/"));
            String c2 = cTmpP.substring(c1.length() + 1, cTmpP.length());
            cTmpP = c2 + "/" + sisi.General.paddingString(c1, 8, spazio, true);
        }
        cTmpP = sisi.General.paddingString(cTmpP, 15, spazio, false);
        return cTmpP;
    }

    static final Comparator<Estrattoconto> PARTITA_ORDER = new Comparator<Estrattoconto>() {

        public int compare(Estrattoconto e1, Estrattoconto e2) {
            String cCompare1 = e1.getCodiceNominativo() + formattapartita(e1.getPartita()) + e1.getNumeroxOrdinamento() + e1.getDataDocumento().toString();
            String cCompare2 = e2.getCodiceNominativo() + formattapartita(e2.getPartita()) + e2.getNumeroxOrdinamento() + e2.getDataDocumento().toString();
            return cCompare1.compareTo(cCompare2);
        }
    };

    static final Comparator<Estrattoconto> PARTITA_NOMINATIVO_ORDER = new Comparator<Estrattoconto>() {

        public int compare(Estrattoconto e1, Estrattoconto e2) {
            String cCompare1 = e1.getNomeNominativo() + formattapartita(e1.getPartita()) + e1.getNumeroxOrdinamento() + e1.getDataDocumento().toString();
            String cCompare2 = e2.getNomeNominativo() + formattapartita(e2.getPartita()) + e2.getNumeroxOrdinamento() + e2.getDataDocumento().toString();
            return cCompare1.compareTo(cCompare2);
        }
    };

    static final Comparator<Estrattoconto> PARTITA_CITTA_NOMINATIVO_ORDER = new Comparator<Estrattoconto>() {

        public int compare(Estrattoconto e1, Estrattoconto e2) {
            String citta1 = "";
            if (e1.getNominativo() != null && e1.getNominativo().getNomcitta() != null) {
                citta1 = e1.getNominativo().getNomcitta();
            }
            String citta2 = "";
            if (e2.getNominativo() != null && e2.getNominativo().getNomcitta() != null) {
                citta2 = e2.getNominativo().getNomcitta();
            }
            String cCompare1 = citta1 + e1.getNomeNominativo() + formattapartita(e1.getPartita()) + e1.getNumeroxOrdinamento() + e1.getDataDocumento().toString();
            String cCompare2 = citta2 + e2.getNomeNominativo() + formattapartita(e2.getPartita()) + e2.getNumeroxOrdinamento() + e2.getDataDocumento().toString();
            return cCompare1.compareTo(cCompare2);
        }
    };

    static final Comparator<Estrattoconto> DATADOC_ORDER = new Comparator<Estrattoconto>() {

        public int compare(Estrattoconto e1, Estrattoconto e2) {
            return e1.getDataDocumento().compareTo(e2.getDataDocumento());
        }
    };

    @SuppressWarnings("unchecked")
    public static void Report() {
        try {
            String reportSource = "WebContent/reports/report1_jpa.jrxml";
            String reportDest = "c:/report/report1_jpa.pdf";
            EntityManagerFactory emf2 = new MyEmf().getEmf();
            EntityManager em2 = emf2.createEntityManager();
            Map parameters = new HashMap();
            parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em2);
            String reportFile = "c://tmp//reports//report1_jpa.jrxml";
            JasperDesign jasperDesign2 = JRXmlLoader.load(reportFile);
            JasperReport jasperReport2 = JasperCompileManager.compileReport(jasperDesign2);
            JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport2, parameters);
            JasperViewer.viewReport(jasperPrint2);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public BigDecimal calcoloSaldoPartitaListbox(Listbox box, String partita) {
        BigDecimal saldo = BigDecimal.ZERO;
        new EstrattocontoController().azzeraECSelezionato();
        Object itemEC = box.getSelectedItem().getValue();
        EstrattoContoSelezionato = ((Estrattoconto) itemEC);
        EstrattoContoSelezionato.setSaldo(BigDecimal.ZERO);
        int nLen = box.getItemCount();
        for (int i = 0; i < nLen; i++) {
            Object itemEstrattoConto = box.getItemAtIndex(i).getValue();
            Estrattoconto EC = ((Estrattoconto) itemEstrattoConto);
            if (EC.getPartita().equals(partita) && EC.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                saldo = EC.getSaldo();
                EstrattoContoSelezionato.setSaldo(saldo);
                break;
            }
        }
        return saldo;
    }
}

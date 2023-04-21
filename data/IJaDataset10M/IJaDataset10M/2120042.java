package org.fhw.cabaweb.ojb;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.ojb.broker.metadata.FieldHelper;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.PersistenceBroker;
import org.fhw.cabaweb.ojb.abstracts.AbstractUseCase;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Integer;

/**
 * Abstrakte Klasse f&uuml;r die OJB Kapselung der Datenbankoperationen
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 18.05.2004
 */
public class UseCaseErgebnissdatenInteger extends AbstractUseCase {

    /** Konstruktor
     * 
     * @param broker Instanz des Persistence Brokers
     */
    public UseCaseErgebnissdatenInteger(PersistenceBroker broker) {
        super(broker);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#erzeugen(java.lang.Object)
      */
    public final boolean erzeugen(Object arg) {
        Ergebnissdaten_Integer newErgebnissdaten_Integer = (Ergebnissdaten_Integer) arg;
        return anlegen(newErgebnissdaten_Integer);
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#editieren(java.lang.Object)
      */
    public final boolean editieren(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Integer editErgebnissdaten_IntegerTemp = (Ergebnissdaten_Integer) arg;
        Ergebnissdaten_Integer editErgebnissdaten_Integer = null;
        Collection liste = null;
        Criteria criteria = new Criteria();
        if (editErgebnissdaten_IntegerTemp.getProjektgruppen() != null && editErgebnissdaten_IntegerTemp.getProjektgruppen().getProjekte() != null && editErgebnissdaten_IntegerTemp.getProjektgruppen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", editErgebnissdaten_IntegerTemp.getProjektgruppen().getProjekte().getProjektnummer());
        if (editErgebnissdaten_IntegerTemp.getProjektgruppen() != null && editErgebnissdaten_IntegerTemp.getProjektgruppen().getGruppennummer() != null) criteria.addEqualTo("Gruppennummer", editErgebnissdaten_IntegerTemp.getProjektgruppen().getGruppennummer());
        if (editErgebnissdaten_IntegerTemp.getQuartal() != null) criteria.addEqualTo("Quartal", editErgebnissdaten_IntegerTemp.getQuartal());
        liste = sucheQBC(Ergebnissdaten_Integer.class, criteria, null);
        if (liste != null && liste.size() == 1) editErgebnissdaten_Integer = (Ergebnissdaten_Integer) liste.iterator().next();
        editErgebnissdaten_Integer.setInteger1(editErgebnissdaten_IntegerTemp.getInteger1());
        editErgebnissdaten_Integer.setInteger2(editErgebnissdaten_IntegerTemp.getInteger2());
        editErgebnissdaten_Integer.setInteger3(editErgebnissdaten_IntegerTemp.getInteger3());
        editErgebnissdaten_Integer.setInteger4(editErgebnissdaten_IntegerTemp.getInteger4());
        editErgebnissdaten_Integer.setInteger5(editErgebnissdaten_IntegerTemp.getInteger5());
        editErgebnissdaten_Integer.setInteger6(editErgebnissdaten_IntegerTemp.getInteger6());
        editErgebnissdaten_Integer.setInteger7(editErgebnissdaten_IntegerTemp.getInteger7());
        editErgebnissdaten_Integer.setInteger8(editErgebnissdaten_IntegerTemp.getInteger8());
        editErgebnissdaten_Integer.setInteger9(editErgebnissdaten_IntegerTemp.getInteger9());
        editErgebnissdaten_Integer.setInteger10(editErgebnissdaten_IntegerTemp.getInteger10());
        editErgebnissdaten_Integer.setInteger11(editErgebnissdaten_IntegerTemp.getInteger11());
        editErgebnissdaten_Integer.setInteger12(editErgebnissdaten_IntegerTemp.getInteger12());
        editErgebnissdaten_Integer.setInteger13(editErgebnissdaten_IntegerTemp.getInteger13());
        editErgebnissdaten_Integer.setInteger14(editErgebnissdaten_IntegerTemp.getInteger14());
        editErgebnissdaten_Integer.setInteger15(editErgebnissdaten_IntegerTemp.getInteger15());
        editErgebnissdaten_Integer.setInteger16(editErgebnissdaten_IntegerTemp.getInteger16());
        editErgebnissdaten_Integer.setInteger17(editErgebnissdaten_IntegerTemp.getInteger17());
        editErgebnissdaten_Integer.setInteger18(editErgebnissdaten_IntegerTemp.getInteger18());
        editErgebnissdaten_Integer.setInteger19(editErgebnissdaten_IntegerTemp.getInteger19());
        editErgebnissdaten_Integer.setInteger20(editErgebnissdaten_IntegerTemp.getInteger20());
        editErgebnissdaten_Integer.setInteger21(editErgebnissdaten_IntegerTemp.getInteger21());
        editErgebnissdaten_Integer.setInteger22(editErgebnissdaten_IntegerTemp.getInteger22());
        editErgebnissdaten_Integer.setInteger23(editErgebnissdaten_IntegerTemp.getInteger23());
        editErgebnissdaten_Integer.setInteger24(editErgebnissdaten_IntegerTemp.getInteger24());
        editErgebnissdaten_Integer.setInteger25(editErgebnissdaten_IntegerTemp.getInteger25());
        editErgebnissdaten_Integer.setInteger26(editErgebnissdaten_IntegerTemp.getInteger26());
        editErgebnissdaten_Integer.setInteger27(editErgebnissdaten_IntegerTemp.getInteger27());
        editErgebnissdaten_Integer.setInteger28(editErgebnissdaten_IntegerTemp.getInteger28());
        editErgebnissdaten_Integer.setInteger29(editErgebnissdaten_IntegerTemp.getInteger29());
        editErgebnissdaten_Integer.setInteger30(editErgebnissdaten_IntegerTemp.getInteger30());
        editErgebnissdaten_Integer.setInteger31(editErgebnissdaten_IntegerTemp.getInteger31());
        editErgebnissdaten_Integer.setInteger32(editErgebnissdaten_IntegerTemp.getInteger32());
        editErgebnissdaten_Integer.setInteger33(editErgebnissdaten_IntegerTemp.getInteger33());
        editErgebnissdaten_Integer.setInteger34(editErgebnissdaten_IntegerTemp.getInteger34());
        editErgebnissdaten_Integer.setInteger35(editErgebnissdaten_IntegerTemp.getInteger35());
        editErgebnissdaten_Integer.setInteger36(editErgebnissdaten_IntegerTemp.getInteger36());
        editErgebnissdaten_Integer.setInteger37(editErgebnissdaten_IntegerTemp.getInteger37());
        editErgebnissdaten_Integer.setInteger38(editErgebnissdaten_IntegerTemp.getInteger38());
        editErgebnissdaten_Integer.setInteger39(editErgebnissdaten_IntegerTemp.getInteger39());
        editErgebnissdaten_Integer.setInteger40(editErgebnissdaten_IntegerTemp.getInteger40());
        editErgebnissdaten_Integer.setInteger41(editErgebnissdaten_IntegerTemp.getInteger41());
        editErgebnissdaten_Integer.setInteger42(editErgebnissdaten_IntegerTemp.getInteger42());
        editErgebnissdaten_Integer.setInteger43(editErgebnissdaten_IntegerTemp.getInteger43());
        editErgebnissdaten_Integer.setInteger44(editErgebnissdaten_IntegerTemp.getInteger44());
        editErgebnissdaten_Integer.setInteger45(editErgebnissdaten_IntegerTemp.getInteger45());
        editErgebnissdaten_Integer.setInteger46(editErgebnissdaten_IntegerTemp.getInteger46());
        editErgebnissdaten_Integer.setInteger47(editErgebnissdaten_IntegerTemp.getInteger47());
        editErgebnissdaten_Integer.setInteger48(editErgebnissdaten_IntegerTemp.getInteger48());
        editErgebnissdaten_Integer.setInteger49(editErgebnissdaten_IntegerTemp.getInteger49());
        editErgebnissdaten_Integer.setInteger50(editErgebnissdaten_IntegerTemp.getInteger50());
        editErgebnissdaten_Integer.setInteger51(editErgebnissdaten_IntegerTemp.getInteger51());
        editErgebnissdaten_Integer.setInteger52(editErgebnissdaten_IntegerTemp.getInteger52());
        editErgebnissdaten_Integer.setInteger53(editErgebnissdaten_IntegerTemp.getInteger53());
        editErgebnissdaten_Integer.setInteger54(editErgebnissdaten_IntegerTemp.getInteger54());
        editErgebnissdaten_Integer.setInteger55(editErgebnissdaten_IntegerTemp.getInteger55());
        editErgebnissdaten_Integer.setInteger56(editErgebnissdaten_IntegerTemp.getInteger56());
        editErgebnissdaten_Integer.setInteger57(editErgebnissdaten_IntegerTemp.getInteger57());
        editErgebnissdaten_Integer.setInteger58(editErgebnissdaten_IntegerTemp.getInteger58());
        editErgebnissdaten_Integer.setInteger59(editErgebnissdaten_IntegerTemp.getInteger59());
        editErgebnissdaten_Integer.setInteger60(editErgebnissdaten_IntegerTemp.getInteger60());
        editErgebnissdaten_Integer.setInteger61(editErgebnissdaten_IntegerTemp.getInteger61());
        editErgebnissdaten_Integer.setInteger62(editErgebnissdaten_IntegerTemp.getInteger62());
        editErgebnissdaten_Integer.setInteger63(editErgebnissdaten_IntegerTemp.getInteger63());
        editErgebnissdaten_Integer.setInteger64(editErgebnissdaten_IntegerTemp.getInteger64());
        editErgebnissdaten_Integer.setInteger65(editErgebnissdaten_IntegerTemp.getInteger65());
        editErgebnissdaten_Integer.setInteger66(editErgebnissdaten_IntegerTemp.getInteger66());
        editErgebnissdaten_Integer.setInteger67(editErgebnissdaten_IntegerTemp.getInteger67());
        editErgebnissdaten_Integer.setInteger68(editErgebnissdaten_IntegerTemp.getInteger68());
        editErgebnissdaten_Integer.setInteger69(editErgebnissdaten_IntegerTemp.getInteger69());
        editErgebnissdaten_Integer.setInteger70(editErgebnissdaten_IntegerTemp.getInteger70());
        editErgebnissdaten_Integer.setInteger71(editErgebnissdaten_IntegerTemp.getInteger71());
        editErgebnissdaten_Integer.setInteger72(editErgebnissdaten_IntegerTemp.getInteger72());
        editErgebnissdaten_Integer.setInteger73(editErgebnissdaten_IntegerTemp.getInteger73());
        editErgebnissdaten_Integer.setInteger74(editErgebnissdaten_IntegerTemp.getInteger74());
        editErgebnissdaten_Integer.setInteger75(editErgebnissdaten_IntegerTemp.getInteger75());
        editErgebnissdaten_Integer.setInteger76(editErgebnissdaten_IntegerTemp.getInteger76());
        editErgebnissdaten_Integer.setInteger77(editErgebnissdaten_IntegerTemp.getInteger77());
        editErgebnissdaten_Integer.setInteger78(editErgebnissdaten_IntegerTemp.getInteger78());
        editErgebnissdaten_Integer.setInteger79(editErgebnissdaten_IntegerTemp.getInteger79());
        editErgebnissdaten_Integer.setInteger80(editErgebnissdaten_IntegerTemp.getInteger80());
        editErgebnissdaten_Integer.setInteger81(editErgebnissdaten_IntegerTemp.getInteger81());
        editErgebnissdaten_Integer.setInteger82(editErgebnissdaten_IntegerTemp.getInteger82());
        editErgebnissdaten_Integer.setInteger83(editErgebnissdaten_IntegerTemp.getInteger83());
        editErgebnissdaten_Integer.setInteger84(editErgebnissdaten_IntegerTemp.getInteger84());
        editErgebnissdaten_Integer.setInteger85(editErgebnissdaten_IntegerTemp.getInteger85());
        editErgebnissdaten_Integer.setInteger86(editErgebnissdaten_IntegerTemp.getInteger86());
        editErgebnissdaten_Integer.setInteger87(editErgebnissdaten_IntegerTemp.getInteger87());
        editErgebnissdaten_Integer.setInteger88(editErgebnissdaten_IntegerTemp.getInteger88());
        editErgebnissdaten_Integer.setInteger89(editErgebnissdaten_IntegerTemp.getInteger89());
        editErgebnissdaten_Integer.setInteger90(editErgebnissdaten_IntegerTemp.getInteger90());
        editErgebnissdaten_Integer.setInteger91(editErgebnissdaten_IntegerTemp.getInteger91());
        editErgebnissdaten_Integer.setInteger92(editErgebnissdaten_IntegerTemp.getInteger92());
        editErgebnissdaten_Integer.setInteger93(editErgebnissdaten_IntegerTemp.getInteger93());
        editErgebnissdaten_Integer.setInteger94(editErgebnissdaten_IntegerTemp.getInteger94());
        editErgebnissdaten_Integer.setInteger95(editErgebnissdaten_IntegerTemp.getInteger95());
        editErgebnissdaten_Integer.setInteger96(editErgebnissdaten_IntegerTemp.getInteger96());
        editErgebnissdaten_Integer.setInteger97(editErgebnissdaten_IntegerTemp.getInteger97());
        editErgebnissdaten_Integer.setInteger98(editErgebnissdaten_IntegerTemp.getInteger98());
        editErgebnissdaten_Integer.setInteger99(editErgebnissdaten_IntegerTemp.getInteger99());
        editErgebnissdaten_Integer.setInteger100(editErgebnissdaten_IntegerTemp.getInteger100());
        editErgebnissdaten_Integer.setInteger101(editErgebnissdaten_IntegerTemp.getInteger101());
        editErgebnissdaten_Integer.setInteger102(editErgebnissdaten_IntegerTemp.getInteger102());
        editErgebnissdaten_Integer.setInteger103(editErgebnissdaten_IntegerTemp.getInteger103());
        editErgebnissdaten_Integer.setInteger104(editErgebnissdaten_IntegerTemp.getInteger104());
        editErgebnissdaten_Integer.setInteger105(editErgebnissdaten_IntegerTemp.getInteger105());
        editErgebnissdaten_Integer.setInteger106(editErgebnissdaten_IntegerTemp.getInteger106());
        editErgebnissdaten_Integer.setInteger107(editErgebnissdaten_IntegerTemp.getInteger107());
        editErgebnissdaten_Integer.setInteger108(editErgebnissdaten_IntegerTemp.getInteger108());
        editErgebnissdaten_Integer.setInteger109(editErgebnissdaten_IntegerTemp.getInteger109());
        editErgebnissdaten_Integer.setInteger110(editErgebnissdaten_IntegerTemp.getInteger110());
        editErgebnissdaten_Integer.setInteger111(editErgebnissdaten_IntegerTemp.getInteger111());
        editErgebnissdaten_Integer.setInteger112(editErgebnissdaten_IntegerTemp.getInteger112());
        editErgebnissdaten_Integer.setInteger113(editErgebnissdaten_IntegerTemp.getInteger113());
        editErgebnissdaten_Integer.setInteger114(editErgebnissdaten_IntegerTemp.getInteger114());
        editErgebnissdaten_Integer.setInteger115(editErgebnissdaten_IntegerTemp.getInteger115());
        editErgebnissdaten_Integer.setInteger116(editErgebnissdaten_IntegerTemp.getInteger116());
        editErgebnissdaten_Integer.setInteger117(editErgebnissdaten_IntegerTemp.getInteger117());
        editErgebnissdaten_Integer.setInteger118(editErgebnissdaten_IntegerTemp.getInteger118());
        editErgebnissdaten_Integer.setInteger119(editErgebnissdaten_IntegerTemp.getInteger119());
        editErgebnissdaten_Integer.setInteger120(editErgebnissdaten_IntegerTemp.getInteger120());
        editErgebnissdaten_Integer.setInteger121(editErgebnissdaten_IntegerTemp.getInteger121());
        editErgebnissdaten_Integer.setInteger122(editErgebnissdaten_IntegerTemp.getInteger122());
        editErgebnissdaten_Integer.setInteger123(editErgebnissdaten_IntegerTemp.getInteger123());
        editErgebnissdaten_Integer.setInteger124(editErgebnissdaten_IntegerTemp.getInteger124());
        editErgebnissdaten_Integer.setInteger125(editErgebnissdaten_IntegerTemp.getInteger125());
        editErgebnissdaten_Integer.setInteger126(editErgebnissdaten_IntegerTemp.getInteger126());
        editErgebnissdaten_Integer.setInteger127(editErgebnissdaten_IntegerTemp.getInteger127());
        editErgebnissdaten_Integer.setInteger128(editErgebnissdaten_IntegerTemp.getInteger128());
        editErgebnissdaten_Integer.setInteger129(editErgebnissdaten_IntegerTemp.getInteger129());
        editErgebnissdaten_Integer.setInteger130(editErgebnissdaten_IntegerTemp.getInteger130());
        editErgebnissdaten_Integer.setInteger131(editErgebnissdaten_IntegerTemp.getInteger131());
        editErgebnissdaten_Integer.setInteger132(editErgebnissdaten_IntegerTemp.getInteger132());
        editErgebnissdaten_Integer.setInteger133(editErgebnissdaten_IntegerTemp.getInteger133());
        editErgebnissdaten_Integer.setInteger134(editErgebnissdaten_IntegerTemp.getInteger134());
        editErgebnissdaten_Integer.setInteger135(editErgebnissdaten_IntegerTemp.getInteger135());
        editErgebnissdaten_Integer.setInteger136(editErgebnissdaten_IntegerTemp.getInteger136());
        editErgebnissdaten_Integer.setInteger137(editErgebnissdaten_IntegerTemp.getInteger137());
        editErgebnissdaten_Integer.setInteger138(editErgebnissdaten_IntegerTemp.getInteger138());
        editErgebnissdaten_Integer.setInteger139(editErgebnissdaten_IntegerTemp.getInteger139());
        editErgebnissdaten_Integer.setInteger140(editErgebnissdaten_IntegerTemp.getInteger140());
        editErgebnissdaten_Integer.setInteger141(editErgebnissdaten_IntegerTemp.getInteger141());
        editErgebnissdaten_Integer.setInteger142(editErgebnissdaten_IntegerTemp.getInteger142());
        editErgebnissdaten_Integer.setInteger143(editErgebnissdaten_IntegerTemp.getInteger143());
        editErgebnissdaten_Integer.setInteger144(editErgebnissdaten_IntegerTemp.getInteger144());
        editErgebnissdaten_Integer.setInteger145(editErgebnissdaten_IntegerTemp.getInteger145());
        editErgebnissdaten_Integer.setInteger146(editErgebnissdaten_IntegerTemp.getInteger146());
        editErgebnissdaten_Integer.setInteger147(editErgebnissdaten_IntegerTemp.getInteger147());
        editErgebnissdaten_Integer.setInteger148(editErgebnissdaten_IntegerTemp.getInteger148());
        editErgebnissdaten_Integer.setInteger149(editErgebnissdaten_IntegerTemp.getInteger149());
        editErgebnissdaten_Integer.setInteger150(editErgebnissdaten_IntegerTemp.getInteger150());
        editErgebnissdaten_Integer.setInteger151(editErgebnissdaten_IntegerTemp.getInteger151());
        editErgebnissdaten_Integer.setInteger152(editErgebnissdaten_IntegerTemp.getInteger152());
        editErgebnissdaten_Integer.setInteger153(editErgebnissdaten_IntegerTemp.getInteger153());
        editErgebnissdaten_Integer.setInteger154(editErgebnissdaten_IntegerTemp.getInteger154());
        editErgebnissdaten_Integer.setInteger155(editErgebnissdaten_IntegerTemp.getInteger155());
        editErgebnissdaten_Integer.setInteger156(editErgebnissdaten_IntegerTemp.getInteger156());
        editErgebnissdaten_Integer.setInteger157(editErgebnissdaten_IntegerTemp.getInteger157());
        editErgebnissdaten_Integer.setInteger158(editErgebnissdaten_IntegerTemp.getInteger158());
        editErgebnissdaten_Integer.setInteger159(editErgebnissdaten_IntegerTemp.getInteger159());
        editErgebnissdaten_Integer.setInteger160(editErgebnissdaten_IntegerTemp.getInteger160());
        editErgebnissdaten_Integer.setInteger161(editErgebnissdaten_IntegerTemp.getInteger161());
        editErgebnissdaten_Integer.setInteger162(editErgebnissdaten_IntegerTemp.getInteger162());
        editErgebnissdaten_Integer.setInteger163(editErgebnissdaten_IntegerTemp.getInteger163());
        editErgebnissdaten_Integer.setInteger164(editErgebnissdaten_IntegerTemp.getInteger164());
        editErgebnissdaten_Integer.setInteger165(editErgebnissdaten_IntegerTemp.getInteger165());
        editErgebnissdaten_Integer.setInteger166(editErgebnissdaten_IntegerTemp.getInteger166());
        editErgebnissdaten_Integer.setInteger167(editErgebnissdaten_IntegerTemp.getInteger167());
        editErgebnissdaten_Integer.setInteger168(editErgebnissdaten_IntegerTemp.getInteger168());
        editErgebnissdaten_Integer.setInteger169(editErgebnissdaten_IntegerTemp.getInteger169());
        editErgebnissdaten_Integer.setInteger170(editErgebnissdaten_IntegerTemp.getInteger170());
        editErgebnissdaten_Integer.setInteger171(editErgebnissdaten_IntegerTemp.getInteger171());
        editErgebnissdaten_Integer.setInteger172(editErgebnissdaten_IntegerTemp.getInteger172());
        editErgebnissdaten_Integer.setInteger173(editErgebnissdaten_IntegerTemp.getInteger173());
        editErgebnissdaten_Integer.setInteger174(editErgebnissdaten_IntegerTemp.getInteger174());
        editErgebnissdaten_Integer.setInteger175(editErgebnissdaten_IntegerTemp.getInteger175());
        editErgebnissdaten_Integer.setInteger176(editErgebnissdaten_IntegerTemp.getInteger176());
        editErgebnissdaten_Integer.setInteger177(editErgebnissdaten_IntegerTemp.getInteger177());
        editErgebnissdaten_Integer.setInteger178(editErgebnissdaten_IntegerTemp.getInteger178());
        editErgebnissdaten_Integer.setInteger179(editErgebnissdaten_IntegerTemp.getInteger179());
        editErgebnissdaten_Integer.setInteger180(editErgebnissdaten_IntegerTemp.getInteger180());
        editErgebnissdaten_Integer.setInteger181(editErgebnissdaten_IntegerTemp.getInteger181());
        editErgebnissdaten_Integer.setInteger182(editErgebnissdaten_IntegerTemp.getInteger182());
        editErgebnissdaten_Integer.setInteger183(editErgebnissdaten_IntegerTemp.getInteger183());
        editErgebnissdaten_Integer.setInteger184(editErgebnissdaten_IntegerTemp.getInteger184());
        editErgebnissdaten_Integer.setInteger185(editErgebnissdaten_IntegerTemp.getInteger185());
        editErgebnissdaten_Integer.setInteger186(editErgebnissdaten_IntegerTemp.getInteger186());
        editErgebnissdaten_Integer.setInteger187(editErgebnissdaten_IntegerTemp.getInteger187());
        editErgebnissdaten_Integer.setInteger188(editErgebnissdaten_IntegerTemp.getInteger188());
        editErgebnissdaten_Integer.setInteger189(editErgebnissdaten_IntegerTemp.getInteger189());
        editErgebnissdaten_Integer.setInteger190(editErgebnissdaten_IntegerTemp.getInteger190());
        editErgebnissdaten_Integer.setInteger191(editErgebnissdaten_IntegerTemp.getInteger191());
        editErgebnissdaten_Integer.setInteger192(editErgebnissdaten_IntegerTemp.getInteger192());
        editErgebnissdaten_Integer.setInteger193(editErgebnissdaten_IntegerTemp.getInteger193());
        editErgebnissdaten_Integer.setInteger194(editErgebnissdaten_IntegerTemp.getInteger194());
        editErgebnissdaten_Integer.setInteger195(editErgebnissdaten_IntegerTemp.getInteger195());
        editErgebnissdaten_Integer.setInteger196(editErgebnissdaten_IntegerTemp.getInteger196());
        editErgebnissdaten_Integer.setInteger197(editErgebnissdaten_IntegerTemp.getInteger197());
        editErgebnissdaten_Integer.setInteger198(editErgebnissdaten_IntegerTemp.getInteger198());
        editErgebnissdaten_Integer.setInteger199(editErgebnissdaten_IntegerTemp.getInteger199());
        editErgebnissdaten_Integer.setInteger200(editErgebnissdaten_IntegerTemp.getInteger200());
        retval = beginTransaction();
        if (retval != false) retval = storeAndEndTransaction(editErgebnissdaten_Integer);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#loeschen(java.lang.Object)
      */
    public final boolean loeschen(Object arg) {
        boolean retval = true;
        Ergebnissdaten_Integer deleteErgebnissdaten_Integer = (Ergebnissdaten_Integer) arg;
        Collection liste = null;
        Ergebnissdaten_Integer objekt = null;
        Criteria criteria = new Criteria();
        if (deleteErgebnissdaten_Integer.getProjektgruppen() != null && deleteErgebnissdaten_Integer.getProjektgruppen().getProjekte() != null && deleteErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", deleteErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer());
        if (deleteErgebnissdaten_Integer.getProjektgruppen() != null && deleteErgebnissdaten_Integer.getProjektgruppen().getGruppennummer() != null) criteria.addEqualTo("Gruppennummer", deleteErgebnissdaten_Integer.getProjektgruppen().getGruppennummer());
        if (deleteErgebnissdaten_Integer.getQuartal() != null) criteria.addEqualTo("Quartal", deleteErgebnissdaten_Integer.getQuartal());
        liste = sucheQBC(Ergebnissdaten_Integer.class, criteria, null);
        if (liste != null && liste.size() == 1) objekt = (Ergebnissdaten_Integer) liste.iterator().next();
        retval = beginTransaction();
        if (retval != false) retval = deleteAndEndTransaction(objekt);
        return retval;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekt(java.lang.Object)
      */
    public final Object sucheObjekt(Object arg) {
        Ergebnissdaten_Integer suchErgebnissdaten_Integer = (Ergebnissdaten_Integer) arg;
        Collection liste = null;
        Object rueckgabeWert = null;
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Integer.getProjektgruppen() != null && suchErgebnissdaten_Integer.getProjektgruppen().getProjekte() != null && suchErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer());
        if (suchErgebnissdaten_Integer.getProjektgruppen() != null && suchErgebnissdaten_Integer.getProjektgruppen().getGruppennummer() != null) criteria.addEqualTo("Gruppennummer", suchErgebnissdaten_Integer.getProjektgruppen().getGruppennummer());
        if (suchErgebnissdaten_Integer.getQuartal() != null) criteria.addEqualTo("Quartal", suchErgebnissdaten_Integer.getQuartal());
        liste = sucheQBC(Ergebnissdaten_Integer.class, criteria, null);
        if (liste != null && liste.size() == 1) rueckgabeWert = (Ergebnissdaten_Integer) liste.iterator().next();
        return rueckgabeWert;
    }

    /**
      * @see org.fhw.cabaweb.ojb.abstracts.AbstractUseCase#sucheObjekte(java.lang.Object)
      */
    public final Collection sucheObjekte(Object arg) {
        Ergebnissdaten_Integer suchErgebnissdaten_Integer = (Ergebnissdaten_Integer) arg;
        Collection rueckgabeWert = null;
        ArrayList orderBy = new ArrayList();
        Criteria criteria = new Criteria();
        if (suchErgebnissdaten_Integer.getProjektgruppen() != null && suchErgebnissdaten_Integer.getProjektgruppen().getProjekte() != null && suchErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer() != null) criteria.addEqualTo("Projektnummer", suchErgebnissdaten_Integer.getProjektgruppen().getProjekte().getProjektnummer());
        if (suchErgebnissdaten_Integer.getProjektgruppen() != null && suchErgebnissdaten_Integer.getProjektgruppen().getGruppennummer() != null) criteria.addEqualTo("Gruppennummer", suchErgebnissdaten_Integer.getProjektgruppen().getGruppennummer());
        if (suchErgebnissdaten_Integer.getQuartal() != null) criteria.addEqualTo("Quartal", suchErgebnissdaten_Integer.getQuartal());
        orderBy.add(0, new FieldHelper("Projektnummer", true));
        orderBy.add(1, new FieldHelper("Gruppennummer", true));
        orderBy.add(2, new FieldHelper("Quartal", true));
        rueckgabeWert = sucheQBC(Ergebnissdaten_Integer.class, criteria, orderBy);
        return rueckgabeWert;
    }
}

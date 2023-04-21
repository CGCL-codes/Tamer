package org.project.trunks.utilities;

import java.util.ArrayList;

public class Version {

    private static ArrayList versions;

    public static ArrayList getVersions() {
        return versions;
    }

    public static Version getCurrentVersion() {
        return (Version) versions.get(versions.size() - 1);
    }

    static {
        versions = new ArrayList();
        versions.add(new Version("V 1.0 (23/01/2002)", "Cr�ation de la librairie", "Cr�ation du projet"));
        versions.add(new Version("V 2.0 (13/03/2003)", "Fonctions XML", "Int�gration de fonctions XML"));
        versions.add(new Version("V 2.1 (13/03/2003)", "Classes de parsing XML, bases du mod�le Trunks", "XmlSelect, XMLObject, ParseXmlFile, etc. - Voir package org.project.trunks.xml"));
        versions.add(new Version("V 3.0 (10/04/2003)", "Cr�ation des classes Field, bases du mod�le Trunks", "Voir package org.project.trunks.data"));
        versions.add(new Version("V 4.0 (07/04/2004)", "G�n�ration de documents PDF", "Cr�ation de classes permettant une fusion XML-XSL pour g�n�rer des PDF avec FOP"));
        versions.add(new Version("V 4.1 (02/09/2004)", "Int�gration isol�e de la librairie jxl.jar", "Classes encapsulant JXL, permettant une g�n�ration de documents Excel"));
        versions.add(new Version("V 4.2 (18/02/2005)", "Am�lioration des classes Field, bases du mod�le Trunks"));
        versions.add(new Version("V 5.0 (17/06/2006)", "Am�lioration du package g�n�ration", "Cr�ation de classes permettant une fusion XML-XSL pour g�n�rer des RTF avec jfor ou xmlmind"));
        versions.add(new Version("V 6.0 (27/08/2006)", "Int�gration isol�e de la librairie Rtf2fo.jar", "G�n�ration d'un template XSL � partir d'un RTF"));
        versions.add(new Version("V 6.1 (28/08/2006)", "Cr�ation d'une classe org.project.trunks.generation.GenXsl", "Gestion/remplacement des tags NSI d'un template XSL balis�"));
        versions.add(new Version("V 7.0 (04/09/2006)", "Finalisation des librairies de g�n�ration de documents"));
        versions.add(new Version("V 7.0.1 (15/04/2007)", "[GenXsl] Gestion des images : @TRK_IMAGE", "Possibilit� d'ajouter des images dans un template RTF"));
        versions.add(new Version("V 7.0.2 (31/12/2007)", "[Field] DefaultSearchValue", "Valeur par d�faut pour les crit�res de recherche"));
        versions.add(new Version("V 7.1 (23/03/2008)", "Cr�ation d'une classe org.project.trunks.utilities.ZipUtility", "(un)zipFile et (un)zipDirectory"));
        versions.add(new Version("V 7.2 (12/04/2008)", "[GenXsl] Gestion des sommes : @TRK_SUM", "Possibilit� de sp�cifier des sommes automatiques dans un template RTF"));
        versions.add(new Version("V 7.3 (22/04/2008)", "Compatibilit� WINDOWS/UNIX du package g�n�ration", "Impact : les templates doivent respecter l'encoding utilis� par les classes utilitaires - par d�faut UTF-8"));
        versions.add(new Version("V 7.3.1 (11/05/2008)", "FieldKind BAR", "FieldKind BAR"));
        versions.add(new Version("V 7.3.2 (11/05/2008)", "Ajout d'une GroupByClause dans XmlSelect", "Ajout d'une GroupByClause dans XmlSelect"));
        versions.add(new Version("V 7.4 (17/05/2008)", "[RTF -jFor] GenRtf.writeRTF( String xmlName, String xslName, OutputStream os)", "Ajout de cette m�thode prenant un outputStream"));
        versions.add(new Version("V 7.5 (20/05/2008)", "Gestion du type CLOB", "FieldType.CLOB, ApplicDBManager.setFieldValueFromResultSet"));
        versions.add(new Version("V 7.6 (30/05/2008)", "[Validation XSD] Gestion des messages rapport�s", "Correction dans CReporterHandler"));
        versions.add(new Version("V 7.6.1 (06/06/2008)", "Nouvelle propri�t� min length pour un Field", "Field.minLength"));
        versions.add(new Version("V 7.6.2 (09/06/2008)", "Correction VectorUtil.toString(), StringUtilities.stringArrayToString()", "Eviter les concat�nations et utiliser StringBuffer.append"));
        versions.add(new Version("V 7.6.3 (12/06/2008)", "Ajout d'une classe org.project.trunks.crypto.MD5Encoder", "Classe de cryptage en MD5"));
        versions.add(new Version("V 7.6.4 (13/07/2008)", "Ajout d'un attribut 'headerImageUrl' � un champ", "Field.headerImageUrl, uniquement pour les grilles"));
        versions.add(new Version("V 7.6.5 (29/08/2008)", "DateUtil.getInstance()", "Afin de pouvoir appeler getUniqueTime() de fa�on synchronis�e"));
        versions.add(new Version("V 7.6.6 (31/08/2008)", "Field.Ajax_action", "Afin de pouvoir appeler g�n�riquement une action ajax du web form associ�"));
        versions.add(new Version("V 7.6.7 (04/09/2008)", "FieldType.BLOB", "Nouveau type de champ"));
        versions.add(new Version("V 7.6.8 (14/09/2008)", "Field.POPUP_HEIGHT, POPUP_WIDTH", "Nouvelles propri�t�s d'un champ : POPUP_HEIGHT, POPUP_WIDTH"));
        versions.add(new Version("V 7.6.9 (26/10/2008)", "Field.REGEXP, KEY_MSG_REGEXP, EXPORT_XML", "Nouvelles propri�t�s d'un champ : POPUP_HEIGHT, POPUP_WIDTH, EXPORT_XML"));
        versions.add(new Version("V 7.6.10 (30/10/2008)", "FieldAttribute.clone, LabelValueBean.clone", "Impl�mentation de la m�thode clone de FieldAttribute et de LabelValueBean"));
        versions.add(new Version("V 7.6.11 (30/10/2008)", "Field.visible_char", "Nouvelles propri�t�s d'un champ VISIBLE_CHAR, permettra de placer une valeur de longueur raisonnable dans une tableau HTML, astuce placer un tooltip sur ce champ pour avoir la valeur compl�te"));
        versions.add(new Version("V 7.7 (07/11/2008)", "[Correction] Field.getNumberFormatter", "Le grouping separator ne peut �tre vide"));
        versions.add(new Version("V 7.8 (16/11/2008)", "[Am�lioration] Gestion des TRK_HTML_TAG", "Inclusion des donn�es XSL-FO obtenues � partir d'un texte HTML en template(s) suppl�mentaire(s)"));
        versions.add(new Version("V 7.8.1 (29/12/2008)", "[Am�lioration] Ne plus directement passer un NUMBER en INTEGER si pr�cision = 0", "En effet, un int est limit� � +- 2 milliards"));
        versions.add(new Version("V 7.8.2 (02/01/2009)", "Field.hsCustomObjectParams", "Permettront de stocker des infos diverses personnalis�es sans devoir avec une classe sp�cifique"));
        versions.add(new Version("V 7.8.3 (05/01/2009)", "Field.vButtonAction", "Permettront de d�finir un action pour un champ ( un bouton/image � c�t� du champ )"));
        versions.add(new Version("V 7.8.4 (23/01/2009)", "Field.onBlurEvent", "Gestion de l'�v�nement OnBlur"));
        versions.add(new Version("V 7.8.5 (31/01/2009)", "FormatException", "Nouvelle classe FormatException"));
        versions.add(new Version("V 7.8.6 (20/02/2009)", "[Am�lioration] SearchField.custom_expression et repeat", "Nouvelles propri�t�s custom_expression et repeat pour un SearchField"));
        versions.add(new Version("V 7.8.7 (25/03/2009)", "[GenXsl] Somme avec format : @TRK_SUM_F", "Somme avec formatage, attention aux param�tres � s�parer par des ';'"));
        versions.add(new Version("V 7.8.8 (06/04/2009)", "Field.onKeyDown", "Gestion de l'�v�nement OnKeyDown, gestion des touches multidirectionnelles"));
        versions.add(new Version("V 7.8.9 (11/04/2009)", "Field.onKeyUp", "Ev�nement OnKeyUp pour notamment la taille d'un TEXTAREA"));
        versions.add(new Version("V 7.8.10 (22/04/2009)", "XmlSelect.vTableToSave", "Permettra de d�finir si on doit g�rer la sauvegarde sur plusieurs tables, par d�faut, sur la mainTable"));
        versions.add(new Version("V 7.8.11 (11/07/2009)", "GenXml - m�thodes et propri�t�s rendues 'public static'", "GenXml - m�thodes et propri�t�s rendues 'public static'"));
        versions.add(new Version("V 7.9 (18/08/2009)", "DBCP - mise en place d'un nouveau pool de connexions", "Nouvelles librairies, �volution de NsiConnectionManager, nouvelle classe SngConnectionPoolManger, ce pool corrige l'erreur des statements non commit�s et non ferm�s."));
        versions.add(new Version("V 7.9.1 (22/09/2009)", "[GenPdf] processXSLContent - lecture du XSL par le parser de DOM", "Lecture du XSL par le parser de DOM"));
        versions.add(new Version("V 7.9.2 (22/10/2009)", "[DBManager] tryGetConnection - effectuer 3 tentatives en cas d'erreur 'Il n'y a plus de donn�es � lire dans le socket'", "Mise en place d'une m�thode tryGetConnection"));
        versions.add(new Version("V 7.9.3 (22/01/2010)", "Ajout d'un KIND RADIOBUTTON dans les FORM", "Ajout d'un KIND RADIOBUTTON dans les FORM"));
        versions.add(new Version("V 7.9.4 (03/02/2010)", "[DBManager] tryGetConnection - effectuer 3 tentatives en cas d'erreur", "Mise en place d'une m�thode tryGetConnection - Externalisation possible du GET_CONNECTION.NB_MAX_TRY"));
        versions.add(new Version("V 7.9.5 (11/02/2010)", "[GenExcel] Gestion des tooltips convertis en notes", "Gestion des tooltips (cell comment) dans les rapports Excel. N�cessite JXL 2.6"));
        versions.add(new Version("V 7.9.6 (02/03/2010)", "[DBValueConverter] Mise en place d'une classe permettant d'�tre surcharg�e pour encoder/d�coder des valeurs de la DB", "La classe est membre statique de DBManager dont h�rite tous les classes d'acc�s DB"));
        versions.add(new Version("V 8.0.0 (02/03/2010)", "[Int�gration nouvelles versions RTF2FO et XMLMIND] Cr�ation d'une classe GenXfc", "Voir GenXfc"));
        versions.add(new Version("V 8.0.1 (02/03/2010)", "[Evolution] Dictionnary.setFieldProperties", "Gestion des NVARCHAR"));
        versions.add(new Version("V 8.0.2 (03/11/2010)", "[Evolution][GenExcel] Placer les nombres dans des cellules de type Number", ""));
        versions.add(new Version("V 8.0.3 (15/11/2010)", "[Am�lioration] Field.hsCustomObjectParams is now managed in copyProperties", ""));
        versions.add(new Version("V 8.0.4 (16/11/2010)", "[Am�lioration] Modication de la valeur d'un champ avant la sauvegarde (add/update)", "ApplicDBManager.setPreparedStatementValue - SAVE.UPPER/SAVE.LOWER - Propri�t� d'un champ d�finie via CUSTOM_OBJECT_PARAMS"));
        versions.add(new Version("V 8.0.5 (17/11/2010)", "[Am�lioration] [GenXsl] @TRK_SUM_E", "Somme format�e au format europ�en, ex: 1 000,99 (le format est #.# reste param�trable)"));
        versions.add(new Version("V 8.0.6 (05/01/2011)", "[Evolution] [ApplicDBManager] Notion de supplWhereClause", ""));
        versions.add(new Version("V 8.0.7 (02/03/2011)", "[Am�lioration/correction] [Field/FieldHelper] Valeurs pour les champs BETWEEN", "String betweenValInf, String betweenValSup"));
    }

    public static void main(String[] args) {
        System.out.println("NSI-MFR Version : " + Version.getVersion());
        System.out.println("NSI-MFR Commentaire : " + Version.getCurrentVersion().getCommentaire());
        System.out.println("NSI-MFR D�tail : " + Version.getCurrentVersion().getDetail());
    }

    private String libelle;

    private String commentaire;

    private String detail;

    public Version(String pLibelle, String pCommentaire) {
        this(pLibelle, pCommentaire, pCommentaire);
    }

    public Version(String pLibelle, String pCommentaire, String pDetail) {
        libelle = pLibelle;
        commentaire = pCommentaire;
        detail = pDetail;
    }

    public static String getVersion() {
        return ((Version) versions.get(versions.size() - 1)).getLibelle();
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getDetail() {
        return detail;
    }
}

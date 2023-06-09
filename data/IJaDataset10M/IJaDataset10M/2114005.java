package database.ppi;

public class PPIqueries {

    public static final String mint_resultForACnumber = "SELECT fullName, type_full, org_full, id " + "FROM mint_xref x " + "JOIN mint_entries e ON e.id=x.entry " + "WHERE ac=?;";

    public static final String mint_resultForName = "SELECT fullName, type_full, org_full, id " + "FROM mint_entries m " + "WHERE fullName LIKE ?;";

    public static String mint_resultForAlias = "SELECT fullName, type_full, org_full, id " + "FROM mint_alias a " + "JOIN mint_entries e ON e.id=a.entry " + "WHERE alias LIKE ?;";

    public static final String mint_interactionsForID = "SELECT m2.id, m2.shortLabel, m2.fullName, m2.sequence , m3.id, m3.shortLabel, m3.fullName, m3.sequence " + "FROM mint_entries m " + "INNER JOIN mint_entry2interactions i ON i.entry=m.id " + "INNER JOIN mint_interactions_binary mi ON mi.binary_id=i.binary_id " + "INNER JOIN mint_entries m2 ON m2.id=mi.participant_A " + "INNER JOIN mint_entries m3 ON m3.id=mi.participant_B " + "where m.id=?;";

    public static final String mint_complexInteractionsForID = "SELECT m2.id, m2.shortLabel, m2.fullName, m2.sequence , m3.id, m3.shortLabel, m3.fullName, m3.sequence " + "FROM mint_entries m " + "INNER JOIN mint_entry2interactions i ON i.entry=m.id " + "INNER JOIN mint_interactions_complex mi ON mi.complex_id=i.complex_id " + "INNER JOIN mint_entries m2 ON m2.id=mi.participant_A " + "INNER JOIN mint_entries m3 ON m3.id=mi.participant_B " + "where m.id=?;";

    public static final String intact_resultForACnumber = "SELECT fullName, type_full, org_full, id " + "FROM intact_xref x " + "JOIN intact_entries e ON e.id=x.entry " + "WHERE ac=?;";

    public static final String intact_resultForName = "SELECT fullName, type_full, org_full, id " + "FROM intact_entries m " + "WHERE fullName LIKE ?;";

    public static String intact_resultForAlias = "SELECT fullName, type_full, org_full, id " + "FROM intact_alias a " + "JOIN intact_entries e ON e.id=a.entry " + "WHERE alias LIKE ?;";

    public static final String intact_interactionsForID = "SELECT m2.id, m2.shortLabel, m2.fullName, m2.sequence , m3.id, m3.shortLabel, m3.fullName, m3.sequence " + "FROM intact_entries m " + "INNER JOIN intact_entry2interactions i ON i.entry=m.id " + "INNER JOIN intact_interactions_binary mi ON mi.binary_id=i.binary_id " + "INNER JOIN intact_entries m2 ON m2.id=mi.participant_A " + "INNER JOIN intact_entries m3 ON m3.id=mi.participant_B " + "where m.id=?;";

    public static final String intact_complexInteractionsForID = "SELECT m2.id, m2.shortLabel, m2.fullName, m2.sequence , m3.id, m3.shortLabel, m3.fullName, m3.sequence " + "FROM intact_entries m " + "INNER JOIN intact_entry2interactions i ON i.entry=m.id " + "INNER JOIN intact_interactions_complex mi ON mi.complex_id=i.complex_id " + "INNER JOIN intact_entries m2 ON m2.id=mi.participant_A " + "INNER JOIN intact_entries m3 ON m3.id=mi.participant_B " + "where m.id=?;";

    public static String hprd_resultForACnumber = "SELECT main_name, geneSymbol, swissprot_id, HPRD_ID " + "FROM hprd_hprd_id_mapping h " + "WHERE h.swissprot_id=?";

    public static final String hprd_resultForName = "SELECT main_name, geneSymbol, swissprot_id, HPRD_ID " + "FROM hprd_hprd_id_mapping h " + "WHERE h.main_name LIKE ?;";

    public static String hprd_resultForAlias = "SELECT main_name, geneSymbol, swissprot_id, HPRD_ID " + "FROM hprd_hprd_id_mapping h " + "WHERE h.geneSymbol LIKE ?";

    public static String hprd_interactionsForID = "SELECT DISTINCT h1.HPRD_ID, h1.geneSymbol, h1.main_name, ps1.seqeunce , " + "h2.HPRD_ID , h2.geneSymbol, h2.main_name, ps2.seqeunce " + "FROM hprd_hprd_id_mapping h1 " + "INNER JOIN hprd_protein_sequences ps1 ON h1.HPRD_ID=ps1.HPRD_ID " + "LEFT OUTER JOIN hprd_protein_protein_ref ppr ON ppr.interactor_1_hprd_id=h1.HPRD_ID " + "INNER JOIN hprd_hprd_id_mapping h2 ON ppr.interactor_2_hprd_id=h2.HPRD_ID " + "INNER JOIN hprd_protein_sequences ps2 ON ps2.HPRD_ID=h2.HPRD_ID " + "WHERE h1.HPRD_ID=? OR h2.HPRD_ID=?;";
}

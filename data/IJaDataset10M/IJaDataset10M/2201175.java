package aml.ramava.data.report;

public class ReportSQLs {

    private static final String PARTICIPANTS_BY_EXIBITION_PRODUCT_BASE_SQL = "select " + "expa.exibition_participant_id, " + "expa.exibition_participant_number, " + "pa.participant_name, " + "expa.exibition_participant_tablo_name, " + "expr.exibition_product_id, " + "pr.product_id, " + "pr.product_name, " + "expapr.exibition_participant_product_amount, " + "expapr.exibition_participant_product_note " + "from " + "exibition_participants expa, " + "participants pa, " + "exibition_products expr, " + "exibition_participant_products expapr, " + "products pr " + "where " + "expa.exibition_participant_id = expapr.exibition_participant_id and " + "expr.exibition_product_id = expapr.exibition_product_id and " + "pa.participant_id = expa.participant_id and  " + "pr.product_id = expr.product_id";

    public static final String PARTICIPANTS_BY_EXIBITION_PRODUCT = PARTICIPANTS_BY_EXIBITION_PRODUCT_BASE_SQL + " and pr.product_id=?";

    public static String generateParticipantsByExibitionsMultiProduct(int productCount) {
        final String productIdString = " pr.product_id=? ";
        StringBuffer buf = new StringBuffer(" and (");
        for (int i = 0; i < productCount; i++) {
            buf.append(productIdString);
            if (i + 1 < productCount) buf.append(" OR ");
        }
        buf.append(") order by expa.exibition_participant_number");
        return PARTICIPANTS_BY_EXIBITION_PRODUCT_BASE_SQL + buf.toString();
    }
}

package uk.co.koeth.brotzeit.android.publicfoodprovider;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import uk.co.koeth.brotzeit.android.model.FoodRecordProxy;
import android.util.Log;

public class KalorientabelleTvFoodProvider extends AbstractPublicFoodRecordsProvider {

    private static String START_TABLE = ">Nahrungsmittel<";

    private static String END_TABLE = "</table";

    private static String START_ROW = "<tr";

    private static String END_ROW = "</tr";

    private static String START_CELL = "<td";

    private static String END_CELL = "</td";

    private static String START_TAG = "<";

    private static String END_TAG = ">";

    public KalorientabelleTvFoodProvider() {
    }

    @Override
    public void loadPublicFoodRecords(String searchTerm) {
        try {
            if (searchTerm != null && !searchTerm.trim().equals("")) {
                ClientResource publicFoodsResource = new ClientResource("http://kalorientabelle.tv/kalorien-suche.html?nahrungsmittel=" + searchTerm.trim());
                Representation resRepresentation = publicFoodsResource.get();
                String response = resRepresentation.getText();
                if (response != null) {
                    parseResponse(response);
                }
            }
        } catch (Exception e) {
            Log.e("AUTSCH", "???", e);
        }
    }

    private void parseResponse(String response) {
        int startTablePos = response.indexOf(START_TABLE);
        int endTablePos = response.indexOf(END_TABLE, startTablePos);
        if (startTablePos > -1 && endTablePos > -1) {
            String table = response.substring(startTablePos, endTablePos);
            Log.i("PARSER-TABLE", table);
            int rowCursor = 0;
            while (true) {
                int startRowPos = table.indexOf(START_ROW, rowCursor);
                if (startRowPos > -1) {
                    int endRowPos = table.indexOf(END_ROW, startRowPos);
                    if (endRowPos > -1) {
                        String row = table.substring(startRowPos, endRowPos);
                        Log.i("PARSER-ROW", row);
                        rowCursor = endRowPos;
                        int cellCursor = 0;
                        FoodRecordProxy publicFoodRecord = null;
                        for (int i = 0; true; i++) {
                            int startCellPos = row.indexOf(START_CELL, cellCursor);
                            if (startCellPos > -1) {
                                int endCellPos = row.indexOf(END_CELL, startCellPos);
                                if (endCellPos > -1) {
                                    String cell = row.substring(startCellPos, endCellPos);
                                    Log.i("PARSER-CELL", cell);
                                    cellCursor = endCellPos;
                                    int startValuePos = cell.indexOf(END_TAG);
                                    if (startValuePos > -1) {
                                        String value = removeTags(cell.substring(startValuePos + 1, cell.length()));
                                        Log.i("PARSER-VALUE", cell);
                                        switch(i) {
                                            case 0:
                                                publicFoodRecord = new FoodRecordProxy(null, null, null, null);
                                                publicFoodRecord.setName(value);
                                                break;
                                            case 1:
                                                publicFoodRecord.setServing(value);
                                                break;
                                            case 2:
                                                try {
                                                    publicFoodRecord.setCalories(Integer.parseInt(value));
                                                } catch (Exception e) {
                                                }
                                                break;
                                        }
                                    }
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (publicFoodRecord != null) {
                            getPublicFoodRecords().add(publicFoodRecord);
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private String removeTags(String withTags) {
        StringBuffer withoutTags = new StringBuffer();
        int tagLastEndPos = 0;
        int tagNextStartPos = withTags.indexOf(START_TAG);
        withoutTags.append(withTags.substring(tagLastEndPos, tagNextStartPos > -1 ? tagNextStartPos : withTags.length()));
        while (tagNextStartPos > -1 && tagLastEndPos > -1) {
            tagLastEndPos = withTags.indexOf(END_TAG, tagNextStartPos);
            tagNextStartPos = withTags.indexOf(START_TAG, tagLastEndPos);
            if (tagLastEndPos > -1) {
                withoutTags.append(withTags.substring(tagLastEndPos + 1, tagNextStartPos > -1 ? tagNextStartPos : withTags.length()));
            }
        }
        String withoutEscapes = withoutTags.toString();
        withoutEscapes = withoutEscapes.replace("&szlig;", "�");
        withoutEscapes = withoutEscapes.replace("&auml;", "�");
        withoutEscapes = withoutEscapes.replace("&ouml;", "�");
        withoutEscapes = withoutEscapes.replace("&uuml;", "�");
        withoutEscapes = withoutEscapes.replace("&Auml;", "�");
        withoutEscapes = withoutEscapes.replace("&Ouml;", "�");
        withoutEscapes = withoutEscapes.replace("&Uuml;", "�");
        return withoutEscapes;
    }
}

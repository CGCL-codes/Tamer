package com.douglasjose.tech.csv;

/**
 * Class used to exemplify the usage of the library
 *
 * @author Douglas Rodrigues
 */
public class UserGuide {

    public static void main(String args[]) throws Exception {
        Csv csv = CsvFactory.createOfficeCsv();
        csv.add(0, 0, "mydata");
        csv.add(1, 1, "anotherdata");
        String content = csv.get(1, 1);
        System.out.println(content);
        csv.remove(1, 1);
        int i = csv.getColumns();
        int j = csv.getRows();
        System.out.println(i + " " + j);
        Csv syncCsv = CsvFactory.synchronizedCsv(csv);
        Csv customCsv = new BasicCsv(new CustomDelimitersCsvParser("'", ";"));
        iterate(csv);
    }

    private static void iterate(Csv csv) {
        String content;
        for (int i = 0; i < csv.getRows(); i++) {
            for (int j = 0; j < csv.getColumns(); j++) {
                content = csv.get(i, j);
                System.out.println(content);
            }
        }
    }
}

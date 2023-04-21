package ar.uba.fi.tonyvaliente.documents;

public class DocumentQueryTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            String str = "(twistos and jamon or queso) or saladix";
            DocumentBooleanQuery query = DocumentBooleanQuery.newInstance(str, null);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

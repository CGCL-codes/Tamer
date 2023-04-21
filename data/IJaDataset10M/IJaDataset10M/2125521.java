package ejp.presenter.api.filters.parameters;

public class StringListParameterTest {

    public static void main(String[] args_) {
        StringListParameter slp = new StringListParameter("test-string-list", "Title", "Tooltip.");
        slp.setValueAsText("org.jboss;another.pkg");
        slp.showInTestFrame();
        slp.setReadOnly();
        slp.showInTestFrame();
        System.exit(0);
    }
}

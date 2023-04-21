package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlGroupSitting extends OATranslatorTestBungeniXmlBase {

    public TestBungeniXmlGroupSitting() {
        super();
        setConfigFilePath("configfiles/configs/config_bungeni_groupsitting.xml");
        setInputDocument("test/testdocs/type-sitting.xml");
        setOutputDocument("test/testresults/test_bungeni_sitting.xml");
        setOutputMetalex("test/testresults/test_bungeni_question.mlx");
        setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
    }
}

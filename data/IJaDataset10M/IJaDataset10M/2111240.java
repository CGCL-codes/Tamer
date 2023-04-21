package bufferings.ktr.i18n.test.client;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	C:/eclipse/workspace/ktr-i18n-test/war/WEB-INF/classes/bufferings/ktr/i18n/test/client/MyMessages.properties'.
 */
public interface MyMessages extends com.google.gwt.i18n.client.Messages {

    /**
   * Translated "default Welcome. I am in the {0}.".
   * 
   * @return translated "default Welcome. I am in the {0}."
   */
    @DefaultMessage("default Welcome. I am in the {0}.")
    @Key("welcome")
    String welcome(String arg0);
}

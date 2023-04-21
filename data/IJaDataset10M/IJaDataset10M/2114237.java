package mesatests.pix;

import mesatests.MesaTestLogger;
import mesatests.xds.TestKit;
import com.misyshealthcare.connect.ihe.configuration.ConfigurationLoader;
import com.misyshealthcare.connect.ihe.pix.PixIdentifierException;
import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * @author Jim Firby
 * @version MESA 2005 - Nov 21, 2005
 */
public class Test10503 {

    public static void main(String[] args) {
        String test = "10503";
        MesaTestLogger logger = new MesaTestLogger(System.out);
        logger.writeTestBegin(test);
        TestKit.configActor(logger, "pix_q_mesa");
        ConfigurationLoader loader = ConfigurationLoader.getInstance();
        ConfigurationLoader.ActorDescription actor = loader.getDescriptionById("pix_q_mesa");
        IConnectionDescription connection = actor.getConnection();
        TestPixQuery tester = new TestPixQuery(connection, logger);
        try {
            if (tester.sendPixQuery("ABC10503", "Authority3", true)) {
                System.out.println("An error should have been generated");
                System.out.println("Test aborted!");
                return;
            }
        } catch (PixIdentifierException e) {
            logger.writeString("PIX warning 204 - unknown identifier, as expected!");
        }
        logger.writeTestEnd(test);
    }
}

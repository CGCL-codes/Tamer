package demo.pkcs.pkcs11;

import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Notify;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.wrapper.PKCS11Exception;

/**
 * This demo program tries to set a callback Notify handler.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 0.1
 * @invariants
 */
public class NotifyTest implements Notify {

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }
        try {
            Module pkcs11Module = Module.getInstance(args[0]);
            pkcs11Module.initialize(null);
            Slot[] slots = pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
            if (slots.length == 0) {
                System.out.println("No slot with present token found!");
                System.exit(0);
            }
            Slot selectedSlot = slots[0];
            Token token = selectedSlot.getToken();
            System.out.println("################################################################################");
            System.out.print("trying to set Notify callback hanlder... ");
            NotifyTest callback = new NotifyTest();
            String applicationData = "Hello Application!";
            Session session = token.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION, applicationData, callback);
            System.out.println("finished");
            System.out.println("################################################################################");
            session.closeSession();
            pkcs11Module.finalize(null);
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }

    public static void printUsage() {
        System.out.println("Usage: NotifyTest <PKCS#11 module>");
        System.out.println(" e.g.: NotifyTest pk2priv.dll");
        System.out.println("The given DLL must be in the search path of the system.");
    }

    public void notify(Session session, boolean surrender, Object application) throws PKCS11Exception {
        System.out.println("we got a Notify callback !!!");
    }
}

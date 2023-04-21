package nl.adaptivity.printProto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import nl.adaptivity.adapt.AdaptationDescription;
import nl.adaptivity.adapt.Event;
import nl.adaptivity.adapt.UserModel;
import nl.adaptivity.adapt.valueTypes.EventValueType;
import nl.adaptivity.adapt.xml.ADHandler;
import nl.adaptivity.adapt.xml.UMHandler;
import nl.adaptivity.adapt.xml.UMReader;

/**
 * @author Paul de Vrieze
 */
public final class EventPost {

    private EventPost() {
    }

    /**
   * The main function
   * 
   * @param pArgs the arguments
   */
    public static final void main(final String[] pArgs) {
        if (pArgs.length == 1) {
            final int count = Integer.parseInt(pArgs[0]);
            for (int i = 0; i < count; i++) {
                final int rand = (int) Math.floor(Math.random() * (100 * 4 * 2 * 2));
                final boolean duplex = (rand & 1) == 0;
                final boolean staple = ((rand >> 1) & 1) == 0;
                String source;
                switch(((rand >> 2) & 3)) {
                    case 0:
                        source = "dvips";
                        break;
                    case 1:
                        source = "presentation";
                        break;
                    case 2:
                        source = "oocalc";
                        break;
                    default:
                        source = "foo";
                }
                final int pageCount = rand >> 4;
                System.out.print("EVENT(print[source = \"");
                System.out.print(source);
                System.out.print("\", duplex = ");
                System.out.print(duplex);
                System.out.print(", staple = ");
                System.out.print(staple);
                System.out.print(", pageCount = ");
                System.out.print(pageCount);
                System.out.println("])");
            }
            System.exit(0);
        }
        if (pArgs.length != 3) {
            System.err.println("invoke: EventPost <adaptationdescription> <usermodel> <events>");
            System.exit(1);
        }
        final File adaptationDescriptionFile = new File(pArgs[0]);
        final File userModelFile = new File(pArgs[1]);
        final File eventsFile = new File(pArgs[2]);
        try {
            final AdaptationDescription am = ADHandler.getAdaptationDescription(adaptationDescriptionFile);
            UserModel um;
            if (userModelFile.canRead()) {
                um = UMHandler.getUserModel(new FileReader(userModelFile), am);
            } else {
                um = new UserModel(am);
            }
            final BufferedReader eventsReader = new BufferedReader(new FileReader(eventsFile));
            String line;
            final EventValueType eventValueType = new EventValueType();
            eventValueType.setAdaptationDescription(am);
            while ((line = eventsReader.readLine()) != null) {
                final Event foo = eventValueType.fromString(line);
                um.postEvent(foo);
            }
            UMReader.store(new FileWriter(userModelFile), um);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

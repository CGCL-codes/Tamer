package gossipServices.pss;

import gossipServices.basic.nodeDescriptors.ConcreteNodeDescriptor;
import gossipServices.basic.view.ConcretePartialView;
import gossipServices.basic.view.PartialView.PEER_SELECTION_ENUM;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import networkRmi.NetworkManager;

/**
 * 
 * Description: 
 * This factory initializes the Peer Sampling Service.
 *
 */
public class PssFactoryInitializer {

    public static final int VIEW_SIZE = 7;

    private ConcretePeerSamplingService pss;

    private ConcretePartialView view;

    public final ConcretePartialView getView() {
        return view;
    }

    private String nodeName;

    private int hugeAgeValue = 1000;

    public boolean initializePss(String nodeName, String viewInitName, NetworkManager networkManager) {
        this.nodeName = nodeName;
        view = new ConcretePartialView(VIEW_SIZE);
        naiveViewInit(viewInitName);
        System.out.println(view);
        pss = new ConcretePeerSamplingService(2, 2, true, true, PEER_SELECTION_ENUM.rand, view, networkManager);
        return true;
    }

    /**
	 * Fills the actual initial view with all NodeDescriptors of the current
	 * node with a very huge age in order to let them be substitute during the
	 * first gossip swaps
	 */
    @Deprecated
    private void naiveViewInit() {
        try {
            for (int i = 0; i < VIEW_SIZE; i++) {
                view.addNodeDescriptor(new ConcreteNodeDescriptor(nodeName, InetAddress.getLocalHost(), new Integer(hugeAgeValue)));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void naiveViewInit(String inputViewFileName) {
        try {
            FileReader fileReader = new FileReader(inputViewFileName);
            Scanner scan = new Scanner(fileReader);
            for (int i = 0; i < VIEW_SIZE; i++) {
                ConcreteNodeDescriptor node = new ConcreteNodeDescriptor(scan.next(), InetAddress.getByName(scan.next()), new Integer(scan.next()));
                view.addNodeDescriptor(node);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public PeerSamplingService getPss() {
        return pss;
    }
}

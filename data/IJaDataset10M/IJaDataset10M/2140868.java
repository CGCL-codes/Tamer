package router.policy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import router.*;
import packet.flit.header.*;
import router.InputFifo;

public class OddEvenMinimal_Policy extends Policy implements Serializable {

    public static int num_decisions = 0;

    public static int num_routes = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(num_decisions);
        out.writeInt(num_routes);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        num_decisions = in.readInt();
        num_routes = in.readInt();
    }

    public OddEvenMinimal_Policy(Router r) {
        super(r);
    }

    public int getPolicyCode() {
        return 0;
    }

    public String getPolicyDescription() {
        return "Odd-Even Minimal Routing";
    }

    public String getPolicyName() {
        return "Odd-Even";
    }

    protected void find_aval_dir(int input, int dest_x, int dest_y, String[] in_alias, boolean[] aval_dir) {
        if (dest_x == 0) {
            if (dest_y < 0) aval_dir[north] = true; else aval_dir[south] = true;
        } else {
            if (dest_x > 0) {
                if (dest_y == 0) aval_dir[east] = true; else {
                    if (dest_y > 0) {
                        aval_dir[east] = true;
                        if (((getRouter().getAbsAddress().getX() % 2) == 1) && (dest_x == 1)) aval_dir[east] = false;
                        if ((getRouter().getAbsAddress().getX() % 2) == 1) aval_dir[south] = true; else if (in_alias[input].equalsIgnoreCase("north")) aval_dir[south] = true; else if (in_alias[input].equalsIgnoreCase("inject")) aval_dir[south] = true;
                    } else {
                        aval_dir[east] = true;
                        if (((getRouter().getAbsAddress().getX() % 2) == 1) && (dest_x == 1)) aval_dir[east] = false;
                        if ((getRouter().getAbsAddress().getX() % 2) == 1) aval_dir[north] = true; else if (in_alias[input].equalsIgnoreCase("south")) aval_dir[north] = true; else if (in_alias[input].equalsIgnoreCase("inject")) aval_dir[north] = true;
                    }
                }
            } else {
                if (dest_y == 0) aval_dir[west] = true; else {
                    if (dest_y > 0) {
                        aval_dir[south] = true;
                        if ((getRouter().getAbsAddress().getX() % 2) == 1) aval_dir[south] = false;
                        if ((getRouter().getAbsAddress().getX() % 2) == 0) aval_dir[west] = true; else if (in_alias[input].equalsIgnoreCase("east")) aval_dir[west] = true; else if (in_alias[input].equalsIgnoreCase("inject")) aval_dir[west] = true;
                    } else {
                        aval_dir[north] = true;
                        if ((getRouter().getAbsAddress().getX() % 2) == 1) aval_dir[north] = false;
                        if ((getRouter().getAbsAddress().getX() % 2) == 0) aval_dir[west] = true; else if (in_alias[input].equalsIgnoreCase("east")) aval_dir[west] = true; else if (in_alias[input].equalsIgnoreCase("inject")) aval_dir[west] = true;
                    }
                }
            }
        }
    }

    protected void make_routing_decision(boolean[] aval_dir, int i, String[] in_alias, String[] out_alias, HeaderFlit myHeaderFlit) {
        int decision_count = 0, first_choice = 0, second_choice = 0, pref_dir = 0;
        boolean ret;
        for (int j = 0; j < 5; j++) {
            if (aval_dir[j]) {
                decision_count++;
                if (decision_count == 1) first_choice = j; else second_choice = j;
            }
        }
        int x = 0, y = 0;
        for (int j = 0; j < myHeaderFlit.getAddress().getFieldCount(); j++) {
            if (myHeaderFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("x")) x = j;
            if (myHeaderFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("y")) y = j;
        }
        if (!getCrossBar().isOutputPortBound(first_choice)) {
            ret = getRouter().move(i, 0, first_choice);
            if (ret) {
                getCrossBar().bindInputToOutput(i, first_choice);
                if ((first_choice == east) || (first_choice == west)) myHeaderFlit.decAddressField(x);
                if ((first_choice == north) || (first_choice == south)) myHeaderFlit.decAddressField(y);
            } else {
                if (decision_count > 1) {
                    if (!getCrossBar().isOutputPortBound(second_choice)) {
                        ret = getRouter().move(i, 0, second_choice);
                        if (ret) {
                            getCrossBar().bindInputToOutput(i, second_choice);
                            if ((second_choice == east) || (second_choice == west)) myHeaderFlit.decAddressField(x);
                            if ((second_choice == north) || (second_choice == south)) myHeaderFlit.decAddressField(y);
                        }
                    }
                }
            }
        } else {
            if (decision_count > 1) {
                if (!getCrossBar().isOutputPortBound(second_choice)) {
                    ret = getRouter().move(i, 0, second_choice);
                    if (ret) {
                        getCrossBar().bindInputToOutput(i, second_choice);
                        if ((second_choice == east) || (second_choice == west)) myHeaderFlit.decAddressField(x);
                        if ((second_choice == north) || (second_choice == south)) myHeaderFlit.decAddressField(y);
                    }
                }
            }
        }
    }

    protected int extract_x_addr(InputFifo[][] input, int i) {
        HeaderFlit myFlit = (HeaderFlit) input[i][0].peek();
        int x = 0, y = 0;
        for (int j = 0; j < myFlit.getAddress().getFieldCount(); j++) {
            if (myFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("x")) x = j;
            if (myFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("y")) y = j;
        }
        int dest_x = myFlit.getAddress().getField(x).getOffsetArg();
        if (myFlit.getAddress().getField(x).getBindArg() == 1) dest_x = -dest_x;
        return dest_x;
    }

    protected int extract_y_addr(InputFifo[][] input, int i) {
        HeaderFlit myFlit = (HeaderFlit) input[i][0].peek();
        int x = 0, y = 0;
        for (int j = 0; j < myFlit.getAddress().getFieldCount(); j++) {
            if (myFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("x")) x = j;
            if (myFlit.getAddress().getField(j).getAlias().equalsIgnoreCase("y")) y = j;
        }
        int dest_y = myFlit.getAddress().getField(y).getOffsetArg();
        if (myFlit.getAddress().getField(y).getBindArg() == 1) dest_y = -dest_y;
        return dest_y;
    }

    public boolean route(InputFifo[][] input, String[] in_alias, String[] out_alias) {
        boolean[] input_serviced = { false, false, false, false, false };
        for (int i = 0; i < 5; i++) {
            if (out_alias[i].equalsIgnoreCase("north")) north = i;
            if (out_alias[i].equalsIgnoreCase("south")) south = i;
            if (out_alias[i].equalsIgnoreCase("east")) east = i;
            if (out_alias[i].equalsIgnoreCase("west")) west = i;
            if (out_alias[i].equalsIgnoreCase("eject")) eject = i;
        }
        move_routed_packets(input, input_serviced);
        for (int i = 0; i < 5; i++) {
            if (!input_serviced[i]) {
                if (input[i][0].peek() != null) {
                    int dest_x = extract_x_addr(input, i);
                    int dest_y = extract_y_addr(input, i);
                    if ((dest_x == 0) && (dest_y == 0)) {
                        if (!getCrossBar().isOutputPortBound(eject)) {
                            boolean ret = getRouter().move(i, 0, eject);
                            if (ret) {
                                getCrossBar().bindInputToOutput(i, eject);
                            }
                        }
                    } else {
                        boolean[] aval_dir = { false, false, false, false, false };
                        find_aval_dir(i, dest_x, dest_y, in_alias, aval_dir);
                        boolean retVal = false;
                        for (int q = 0; q < aval_dir.length; q++) {
                            if (aval_dir[q]) {
                                retVal = true;
                                num_routes++;
                            }
                        }
                        if (!retVal) {
                            System.err.println("ROUTING ERROR:  odd-even routing policy is stuck!");
                            System.err.println("router=>(" + getRouter().getAbsAddress().getX() + "," + getRouter().getAbsAddress().getY() + ")");
                            System.err.println("dest=>(" + dest_x + "," + dest_y + ")");
                            System.err.println("input=>" + in_alias[i]);
                            System.exit(0);
                        }
                        make_routing_decision(aval_dir, i, in_alias, out_alias, (HeaderFlit) input[i][0].peek());
                    }
                }
            }
        }
        num_decisions++;
        return true;
    }
}

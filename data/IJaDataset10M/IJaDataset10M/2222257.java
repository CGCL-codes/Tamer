package eass.verification.leo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import ail.util.AILexception;
import ail.mas.ActionScheduler;
import ail.syntax.Message;
import ail.syntax.Unifier;
import ail.syntax.Action;
import ail.syntax.NumberTermImpl;
import ail.syntax.NumberTerm;
import ail.syntax.Literal;
import ail.syntax.Structure;
import eass.mas.DefaultEASSEnvironment;

/**
 * Specialised environement for the LEO Control example.
 * @author louiseadennis
 *
 */
public class LEOVerificationEnvironment extends DefaultEASSEnvironment {

    boolean testing_movement = true;

    boolean testing_thrusters = false;

    boolean changing_formations = false;

    boolean allthrusters = false;

    boolean allpositions = false;

    boolean line2 = false;

    boolean requesting_positions = false;

    boolean requesting_formation = false;

    boolean formation_line = true;

    boolean formation_square = false;

    boolean all_can_break = false;

    boolean change_ag1 = true;

    boolean change_message_ag1 = true;

    boolean change_aglead = true;

    boolean change_message_aglead = true;

    boolean ag1_close_to_middle = false;

    boolean ag1_plan_to_middle = false;

    boolean ag1_close_to_left = false;

    boolean ag1_plan_to_left = false;

    boolean ag1_close_to_right = false;

    boolean ag1_plan_to_right = false;

    boolean ag1_close_to_topleft = false;

    boolean ag1_plan_to_topleft = false;

    boolean ag1_close_to_topright = false;

    boolean ag1_plan_to_topright = false;

    boolean ag1_close_to_bottomleft = false;

    boolean ag1_plan_to_bottomleft = false;

    boolean ag1_close_to_bottomright = false;

    boolean ag1_plan_to_bottomright = false;

    boolean ag1_broken_x = false;

    boolean ag1_broken_y = false;

    boolean ag1_broken_z = false;

    boolean ag1_tl_1x = false;

    boolean ag1_tl_1y = false;

    boolean ag1_tl_1z = false;

    boolean ag1_tl_2x = false;

    boolean ag1_tl_2y = false;

    boolean ag1_tl_2z = false;

    Literal close_to_middle = new Literal("close_to");

    Literal plan_to_middle = new Literal("get_close_to");

    Literal close_to_left = new Literal("close_to");

    Literal plan_to_left = new Literal("get_close_to");

    Literal close_to_right = new Literal("close_to");

    Literal plan_to_right = new Literal("get_close_to");

    Literal close_to_topleft = new Literal("close_to");

    Literal plan_to_topleft = new Literal("get_close_to");

    Literal close_to_topright = new Literal("close_to");

    Literal plan_to_topright = new Literal("get_close_to");

    Literal close_to_bottomleft = new Literal("close_to");

    Literal plan_to_bottomleft = new Literal("get_close_to");

    Literal close_to_bottomright = new Literal("close_to");

    Literal plan_to_bottomright = new Literal("get_close_to");

    Literal broken_x = new Literal("broken");

    Literal broken_y = new Literal("broken");

    Literal broken_z = new Literal("broken");

    Literal thruster_line_1x = new Literal("thruster_line");

    Literal thruster_line_1y = new Literal("thruster_line");

    Literal thruster_line_1z = new Literal("thruster_line");

    Literal thruster_line_2x = new Literal("thruster_line");

    Literal thruster_line_2y = new Literal("thruster_line");

    Literal thruster_line_2z = new Literal("thruster_line");

    Literal assuming_formation_line = new Literal("assuming_formation");

    Literal assuming_formation_square = new Literal("assuming_formation");

    Literal middle_pos = new Literal("position");

    Literal left_pos = new Literal("position");

    Literal right_pos = new Literal("position");

    Literal topleft_pos = new Literal("position");

    Literal topright_pos = new Literal("position");

    Literal bottomleft_pos = new Literal("position");

    Literal bottomright_pos = new Literal("position");

    Literal none_pos = new Literal("position");

    Literal ag1_maintaining = new Literal("maintaining");

    Literal ag2_maintaining = new Literal("maintaining");

    Literal ag3_maintaining = new Literal("maintaining");

    Literal ag4_maintaining = new Literal("maintaining");

    Literal ag1_ab_broken = new Literal("aborted");

    Literal ag2_ab_broken = new Literal("aborted");

    Literal ag3_ab_broken = new Literal("aborted");

    Literal ag4_ab_broken = new Literal("aborted");

    Literal new_instruction_dline = new Literal("new_instruction");

    Literal new_instruction_dsquare = new Literal("new_instruction");

    Literal drop_line = new Literal("drop_formation");

    Literal drop_square = new Literal("drop_formation");

    Literal send_pos_ag1 = new Literal("send_position");

    Literal send_pos_ag2 = new Literal("send_position");

    Literal send_pos_ag3 = new Literal("send_position");

    Literal send_pos_ag4 = new Literal("send_position");

    NumberTerm one = new NumberTermImpl("1");

    NumberTerm two = new NumberTermImpl("2");

    Literal plan_middle = new Literal("plan_middle");

    Literal plan_left = new Literal("plan_left");

    Literal plan_right = new Literal("plan_right");

    Literal plan_topleft = new Literal("plan_topleft");

    Literal plan_topright = new Literal("plan_topright");

    Literal plan_bottomleft = new Literal("plan_bottomleft");

    Literal plan_bottomright = new Literal("plan_bottomright");

    Literal xthruster = new Literal("x");

    Literal ythruster = new Literal("y");

    Literal zthruster = new Literal("z");

    Literal middle = new Literal("middle");

    Literal left = new Literal("left");

    Literal right = new Literal("right");

    Literal topleft = new Literal("topleft");

    Literal topright = new Literal("topright");

    Literal bottomleft = new Literal("bottomleft");

    Literal bottomright = new Literal("bottomright");

    Literal none = new Literal("none");

    Literal agent1 = new Literal("ag1");

    Literal agent2 = new Literal("ag2");

    Literal agent3 = new Literal("ag3");

    Literal agent4 = new Literal("ag4");

    Literal line = new Literal("line");

    Literal square = new Literal("square");

    Literal thruster_failure = new Literal("thruster_failure");

    Random random = new Random();

    /**
	 * Two performatives, perform and tell.
	 */
    public LEOVerificationEnvironment() {
        super(false);
        ActionScheduler s = new ActionScheduler();
        setScheduler(s);
        addPerceptListener(s);
        init();
    }

    public void addSharedBelief(String agName, Literal per) {
    }

    ;

    public boolean removeSharedBelief(String agName, Literal per) {
        return true;
    }

    public boolean removeUnifiesShared(String agName, Literal per) {
        return true;
    }

    public void init() {
        close_to_middle.addTerm(middle);
        close_to_left.addTerm(left);
        close_to_right.addTerm(right);
        close_to_topleft.addTerm(topleft);
        close_to_topright.addTerm(topright);
        close_to_bottomleft.addTerm(bottomleft);
        close_to_bottomright.addTerm(bottomright);
        plan_to_middle.addTerm(middle);
        plan_to_middle.addTerm(plan_middle);
        plan_to_left.addTerm(left);
        plan_to_left.addTerm(plan_left);
        plan_to_right.addTerm(right);
        plan_to_right.addTerm(plan_right);
        plan_to_topleft.addTerm(topleft);
        plan_to_topleft.addTerm(plan_topleft);
        plan_to_topright.addTerm(topright);
        plan_to_topright.addTerm(plan_topright);
        plan_to_bottomleft.addTerm(bottomleft);
        plan_to_bottomleft.addTerm(plan_bottomleft);
        plan_to_bottomright.addTerm(bottomright);
        plan_to_bottomright.addTerm(plan_bottomright);
        broken_x.addTerm(xthruster);
        broken_y.addTerm(ythruster);
        broken_z.addTerm(zthruster);
        thruster_line_1x.addTerm(xthruster);
        thruster_line_1x.addTerm(one);
        thruster_line_1y.addTerm(ythruster);
        thruster_line_1y.addTerm(one);
        thruster_line_1z.addTerm(zthruster);
        thruster_line_1z.addTerm(one);
        thruster_line_2x.addTerm(xthruster);
        thruster_line_2x.addTerm(two);
        thruster_line_2y.addTerm(ythruster);
        thruster_line_2y.addTerm(two);
        thruster_line_2z.addTerm(zthruster);
        thruster_line_2z.addTerm(two);
        middle_pos.addTerm(middle);
        left_pos.addTerm(left);
        right_pos.addTerm(right);
        topleft_pos.addTerm(topleft);
        topright_pos.addTerm(topright);
        bottomleft_pos.addTerm(bottomleft);
        bottomright_pos.addTerm(bottomright);
        none_pos.addTerm(none);
        ag1_maintaining.addTerm(agent1);
        ag2_maintaining.addTerm(agent2);
        ag3_maintaining.addTerm(agent3);
        ag4_maintaining.addTerm(agent4);
        ag1_ab_broken.addTerm(thruster_failure);
        ag2_ab_broken.addTerm(thruster_failure);
        ag3_ab_broken.addTerm(thruster_failure);
        ag4_ab_broken.addTerm(thruster_failure);
        ag1_ab_broken.addTerm(agent1);
        ag2_ab_broken.addTerm(agent2);
        ag3_ab_broken.addTerm(agent3);
        ag4_ab_broken.addTerm(agent4);
        assuming_formation_line.addTerm(line);
        assuming_formation_square.addTerm(square);
        drop_line.addTerm(line);
        drop_square.addTerm(square);
        new_instruction_dline.addTerm(drop_line);
        new_instruction_dsquare.addTerm(drop_square);
        send_pos_ag1.addTerm(agent1);
        send_pos_ag2.addTerm(agent2);
        send_pos_ag3.addTerm(agent3);
        send_pos_ag4.addTerm(agent4);
    }

    public Set<Message> getMessages(String agName) {
        TreeSet<Message> messages = new TreeSet<Message>();
        if (agName.equals("ag1")) {
            if (change_message_ag1) {
                boolean send_start_line = false;
                boolean send_start_square = false;
                boolean send_middle = false;
                boolean send_left = false;
                boolean send_right = false;
                boolean send_topleft = false;
                boolean send_topright = false;
                boolean send_bottomleft = false;
                boolean send_bottomright = false;
                boolean send_none = false;
                boolean newins_line = false;
                boolean newins_square = false;
                boolean dropline = false;
                boolean dropsquare = false;
                if (testing_movement) {
                    if (requesting_positions) {
                        if (formation_line) {
                            send_middle = random.nextBoolean();
                            if (allpositions) {
                                send_left = random.nextBoolean();
                                send_right = random.nextBoolean();
                            }
                        }
                        if (formation_square) {
                            send_topleft = random.nextBoolean();
                            if (allpositions) {
                                send_topright = random.nextBoolean();
                                send_bottomleft = random.nextBoolean();
                                send_bottomright = random.nextBoolean();
                            }
                        }
                    }
                    if (requesting_formation) {
                        if (formation_line) {
                            send_start_line = random.nextBoolean();
                        }
                        if (formation_square) {
                            send_start_square = random.nextBoolean();
                        }
                    }
                }
                if (testing_thrusters) {
                    if (formation_line) {
                        newins_line = random.nextBoolean();
                    }
                    if (formation_square) {
                        newins_square = random.nextBoolean();
                    }
                }
                if (changing_formations) {
                    dropline = random.nextBoolean();
                    dropsquare = random.nextBoolean();
                }
                if (send_start_line) {
                    Message msg = new Message(2, "aglead", "ag1", assuming_formation_line);
                    messages.add(msg);
                }
                if (send_start_square) {
                    Message msg = new Message(2, "aglead", "ag1", assuming_formation_square);
                    messages.add(msg);
                }
                if (send_middle) {
                    Message msg = new Message(1, "aglead", "ag1", middle_pos);
                    messages.add(msg);
                }
                if (send_left) {
                    Message msg = new Message(1, "aglead", "ag1", left_pos);
                    messages.add(msg);
                }
                if (send_right) {
                    Message msg = new Message(1, "aglead", "ag1", right_pos);
                    messages.add(msg);
                }
                if (send_topleft) {
                    Message msg = new Message(1, "aglead", "ag1", topleft_pos);
                    messages.add(msg);
                }
                if (send_topright) {
                    Message msg = new Message(1, "aglead", "ag1", topright_pos);
                    messages.add(msg);
                }
                if (send_bottomleft) {
                    Message msg = new Message(1, "aglead", "ag1", bottomleft_pos);
                    messages.add(msg);
                }
                if (send_bottomright) {
                    Message msg = new Message(1, "aglead", "ag1", bottomright_pos);
                    messages.add(msg);
                }
                if (send_none) {
                    Message msg = new Message(1, "aglead", "ag1", none_pos);
                    messages.add(msg);
                }
                if (newins_line) {
                    Message msg = new Message(1, "aglead", "ag1", new_instruction_dline);
                    messages.add(msg);
                }
                if (newins_square) {
                    Message msg = new Message(1, "aglead", "ag1", new_instruction_dsquare);
                    messages.add(msg);
                }
                if (dropline) {
                    Message msg = new Message(2, "aglead", "ag1", drop_line);
                    messages.add(msg);
                }
                if (dropsquare) {
                    Message msg = new Message(2, "aglead", "ag1", drop_square);
                    messages.add(msg);
                }
                change_message_ag1 = false;
            }
        }
        if (agName.equals("aglead")) {
            if (change_message_aglead) {
                boolean ag1maintain = false;
                boolean ag2maintain = false;
                boolean ag3maintain = false;
                boolean ag4maintain = false;
                boolean ag1broken = false;
                boolean ag2broken = false;
                boolean ag3broken = false;
                boolean ag4broken = false;
                boolean ag1_sendpos = false;
                boolean ag2_sendpos = false;
                boolean ag3_sendpos = false;
                boolean ag4_sendpos = false;
                ag1maintain = random.nextBoolean();
                ag2maintain = random.nextBoolean();
                ag3maintain = random.nextBoolean();
                ag4maintain = random.nextBoolean();
                if (testing_thrusters) {
                    ag1broken = random.nextBoolean();
                    if (all_can_break) {
                        ag2broken = random.nextBoolean();
                        ag3broken = random.nextBoolean();
                        ag4broken = random.nextBoolean();
                    }
                }
                if (requesting_positions) {
                    ag1_sendpos = random.nextBoolean();
                    ag2_sendpos = random.nextBoolean();
                    ag3_sendpos = random.nextBoolean();
                    ag4_sendpos = random.nextBoolean();
                }
                if (ag1maintain) {
                    Message msg = new Message(1, "ag1", "aglead", ag1_maintaining);
                    messages.add(msg);
                }
                if (ag2maintain) {
                    Message msg = new Message(1, "ag2", "aglead", ag2_maintaining);
                    messages.add(msg);
                }
                if (ag3maintain) {
                    Message msg = new Message(1, "ag3", "aglead", ag3_maintaining);
                    messages.add(msg);
                }
                if (ag4maintain) {
                    Message msg = new Message(1, "ag4", "aglead", ag4_maintaining);
                    messages.add(msg);
                }
                if (ag1broken) {
                    Message msg = new Message(1, "ag1", "aglead", ag1_ab_broken);
                    messages.add(msg);
                }
                if (ag2broken) {
                    Message msg = new Message(1, "ag2", "aglead", ag2_ab_broken);
                    messages.add(msg);
                }
                if (ag3broken) {
                    Message msg = new Message(1, "ag3", "aglead", ag3_ab_broken);
                    messages.add(msg);
                }
                if (ag4broken) {
                    Message msg = new Message(1, "ag4", "aglead", ag4_ab_broken);
                    messages.add(msg);
                }
                if (ag1_sendpos) {
                    Message msg = new Message(2, "ag1", "aglead", send_pos_ag1);
                    messages.add(msg);
                }
                if (ag2_sendpos) {
                    Message msg = new Message(2, "ag2", "aglead", send_pos_ag2);
                    messages.add(msg);
                }
                if (ag3_sendpos) {
                    Message msg = new Message(2, "ag3", "aglead", send_pos_ag3);
                    messages.add(msg);
                }
                if (ag4_sendpos) {
                    Message msg = new Message(2, "ag4", "aglead", send_pos_ag4);
                    messages.add(msg);
                }
                change_message_aglead = false;
            }
        }
        return messages;
    }

    public Set<Structure> getPercepts(String agName, boolean update) {
        TreeSet<Structure> percepts = new TreeSet<Structure>();
        if (agName.equals("ag1")) {
            if (change_ag1) {
                if (testing_movement) {
                    if (formation_line) {
                        ag1_plan_to_middle = random.nextBoolean();
                        ag1_close_to_middle = random.nextBoolean();
                        System.err.println("randoms: " + ag1_plan_to_middle);
                        System.err.println("randoms: " + ag1_close_to_middle);
                    }
                    if (formation_square) {
                        ag1_plan_to_topleft = random.nextBoolean();
                        ag1_close_to_topleft = random.nextBoolean();
                    }
                    if (allpositions) {
                        if (formation_line) {
                            ag1_plan_to_left = random.nextBoolean();
                            ag1_close_to_left = random.nextBoolean();
                            ag1_plan_to_right = random.nextBoolean();
                            ag1_close_to_right = random.nextBoolean();
                        }
                        if (formation_square) {
                            ag1_plan_to_topright = random.nextBoolean();
                            ag1_close_to_topright = random.nextBoolean();
                            ag1_plan_to_bottomright = random.nextBoolean();
                            ag1_close_to_bottomright = random.nextBoolean();
                            ag1_plan_to_bottomleft = random.nextBoolean();
                            ag1_close_to_bottomleft = random.nextBoolean();
                        }
                    }
                }
                if (testing_thrusters) {
                    ag1_broken_x = random.nextBoolean();
                    if (allthrusters) {
                        ag1_broken_y = random.nextBoolean();
                        ag1_broken_z = random.nextBoolean();
                    }
                    ag1_tl_1x = random.nextBoolean();
                    if (allthrusters) {
                        ag1_tl_1y = random.nextBoolean();
                        ag1_tl_1z = random.nextBoolean();
                    }
                    if (line2) {
                        ag1_tl_2x = random.nextBoolean();
                        if (allthrusters) {
                            ag1_tl_2y = random.nextBoolean();
                            ag1_tl_2z = random.nextBoolean();
                        }
                    }
                }
            }
            if (ag1_close_to_middle) {
                percepts.add(close_to_middle);
            }
            if (ag1_plan_to_middle) {
                percepts.add(plan_to_middle);
            }
            if (ag1_close_to_left) {
                percepts.add(close_to_left);
            }
            if (ag1_plan_to_left) {
                percepts.add(plan_to_left);
            }
            if (ag1_close_to_topleft) {
                percepts.add(close_to_topleft);
            }
            if (ag1_plan_to_topleft) {
                percepts.add(plan_to_topleft);
            }
            if (ag1_close_to_right) {
                percepts.add(close_to_right);
            }
            if (ag1_plan_to_right) {
                percepts.add(plan_to_right);
            }
            if (ag1_close_to_topright) {
                percepts.add(close_to_topright);
            }
            if (ag1_plan_to_topright) {
                percepts.add(plan_to_topright);
            }
            if (ag1_close_to_bottomleft) {
                percepts.add(close_to_bottomleft);
            }
            if (ag1_plan_to_bottomleft) {
                percepts.add(plan_to_bottomleft);
            }
            if (ag1_close_to_bottomright) {
                percepts.add(close_to_bottomright);
            }
            if (ag1_plan_to_bottomright) {
                percepts.add(plan_to_bottomright);
            }
            if (ag1_broken_x) {
                percepts.add(broken_x);
            }
            if (ag1_broken_y) {
                percepts.add(broken_y);
            }
            if (ag1_broken_z) {
                percepts.add(broken_z);
            }
            if (ag1_tl_1x) {
                percepts.add(thruster_line_1x);
            }
            if (ag1_tl_1y) {
                percepts.add(thruster_line_1y);
            }
            if (ag1_tl_1z) {
                percepts.add(thruster_line_1z);
            }
            if (ag1_tl_2x) {
                percepts.add(thruster_line_2x);
            }
            if (ag1_tl_2y) {
                percepts.add(thruster_line_2y);
            }
            if (ag1_tl_2z) {
                percepts.add(thruster_line_2z);
            }
            change_ag1 = false;
        } else {
            return null;
        }
        return percepts;
    }

    /**
	 * When a pickup action is executed the environment stores new perceptions
	 * for the agent - that its picked something up and its hands are now longer
	 * empty.
	 */
    public Unifier executeAction(String agName, Action act) throws AILexception {
        Unifier theta = new Unifier();
        System.err.println(agName + " about to do " + act);
        lastAgent = agName;
        lastAction = act;
        change_for(agName);
        return theta;
    }

    public void run() {
        done = true;
        notifyListeners();
    }

    public void eachrun() {
        System.err.println("running");
    }

    public void change_for(String name) {
        if (name.equals("ag1")) {
            change_ag1 = true;
            change_message_ag1 = true;
        }
        if (name.equals("aglead")) {
            change_message_aglead = true;
        }
    }

    public boolean nothingPending(String agName) {
        if (agName.equals("ag1")) {
            return (!change_ag1);
        } else if (agName.equals("aglead")) {
            return (!change_message_aglead);
        } else {
            System.err.println("returning nothing pending for: " + agName);
            return true;
        }
    }

    public boolean separateThread() {
        return false;
    }

    public boolean done() {
        setDone(true);
        return super.done();
    }

    public HashMap<String, String> configure(String[] args) {
        HashMap<String, String> argmap = new HashMap<String, String>();
        for (String arg : args) {
            ArrayList<String> assign = split(arg);
            if (assign.size() > 0) {
                argmap.put(assign.get(0), assign.get(1));
            }
        }
        return argmap;
    }

    public ArrayList<String> split(String input) {
        boolean a = true;
        boolean b = false;
        String first = "";
        String second = "";
        ArrayList<String> output = new ArrayList<String>();
        char delimiter = new String(":").charAt(0);
        for (int i = 0; i < input.length(); i++) {
            Character c = input.charAt(i);
            if (c.equals(delimiter)) {
                a = false;
                b = true;
            } else {
                if (a) {
                    first += c;
                } else {
                    second += c;
                }
            }
        }
        if (b) {
            output.add(first);
            output.add(second);
        }
        return output;
    }

    public void configure(Map<String, String> configuration) {
        if (configuration.containsKey("testing_movement")) {
            testing_movement = Boolean.valueOf(configuration.get("testing_movement"));
        }
        if (configuration.containsKey("testing_thrusters")) {
            testing_thrusters = Boolean.valueOf(configuration.get("testing_thrusters"));
        }
        if (configuration.containsKey("allthrusters")) {
            allthrusters = Boolean.valueOf(configuration.get("allthrusters"));
        }
        if (configuration.containsKey("allpositions")) {
            allpositions = Boolean.valueOf(configuration.get("allpositions"));
        }
        if (configuration.containsKey("line2")) {
            line2 = Boolean.valueOf(configuration.get("line2"));
        }
        if (configuration.containsKey("formation_line")) {
            formation_line = Boolean.valueOf(configuration.get("formation_line"));
        }
        if (configuration.containsKey("formation_square")) {
            formation_square = Boolean.valueOf(configuration.get("formation_square"));
        }
        if (configuration.containsKey("all_can_break")) {
            all_can_break = Boolean.valueOf(configuration.get("all_can_break"));
        }
        if (configuration.containsKey("requesting_positions")) {
            requesting_positions = Boolean.valueOf(configuration.get("requesting_positions"));
        }
        if (configuration.containsKey("requesting_formation")) {
            requesting_formation = Boolean.valueOf(configuration.get("requesting_formation"));
        }
        if (configuration.containsKey("changing_formation")) {
            changing_formations = Boolean.valueOf(configuration.get("changing_formations"));
            if (changing_formations) {
                formation_line = true;
                formation_square = true;
            } else {
                if (configuration.containsKey("initial_formation")) {
                    if (configuration.get("initial_formation").equals("line")) {
                        formation_line = true;
                        formation_square = false;
                    } else {
                        formation_line = false;
                        formation_square = true;
                    }
                } else {
                    formation_line = true;
                    formation_square = false;
                }
            }
        }
    }
}

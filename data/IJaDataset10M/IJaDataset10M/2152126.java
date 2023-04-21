package ch.zarzu.champions.builder.gui;

import java.awt.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.event.*;
import ch.zarzu.champions.builder.*;

;

public class AdvDescriptionBlock extends JLayeredPane implements HyperlinkListener {

    private JTextArea top_left, top_right;

    private JEditorPane text_area;

    private JLabel type_label;

    private SpringLayout layout;

    private SystemLink sys_link;

    private String main_text;

    private AdvDescription adv_description;

    private TitlePhrase title;

    private boolean is_active;

    public AdvDescriptionBlock(AdvDescription adv_d) {
        layout = new SpringLayout();
        setLayout(layout);
        adv_description = adv_d;
        sys_link = SystemLink.getInstance();
        setActive(false);
        draw();
    }

    public void draw() {
        title = new TitlePhrase("");
        type_label = new JLabel();
        type_label.setForeground(Color.WHITE);
        type_label.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        text_area = new JEditorPane();
        DefaultCaret caret = new DefaultCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        text_area.setCaret(caret);
        text_area.setOpaque(false);
        text_area.setFocusable(false);
        text_area.setEditable(false);
        text_area.setContentType("text/html");
        text_area.addHyperlinkListener(this);
        top_left = new JTextArea();
        caret = new DefaultCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        top_left.setCaret(caret);
        top_left.setForeground(Color.WHITE);
        top_left.setOpaque(false);
        top_left.setLineWrap(true);
        top_left.setWrapStyleWord(true);
        top_left.setFocusable(false);
        top_left.setCursor(Cursor.getDefaultCursor());
        top_left.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        top_right = new JTextArea();
        caret = new DefaultCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        top_right.setCaret(caret);
        top_right.setForeground(Color.WHITE);
        top_right.setOpaque(false);
        top_right.setLineWrap(true);
        top_right.setWrapStyleWord(true);
        top_right.setFocusable(false);
        top_right.setCursor(Cursor.getDefaultCursor());
        top_right.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        main_text = "";
        redraw();
    }

    private void redraw() {
        removeAll();
        top_left.setMaximumSize(new Dimension(0, 0));
        top_right.setMaximumSize(new Dimension(0, 0));
        add(title);
        add(type_label);
        add(top_left);
        add(top_right);
        add(text_area);
        layout.putConstraint(SpringLayout.WEST, title, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, top_left, 170, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, top_left, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, top_left, 0, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.EAST, top_right, -10, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, top_right, 170, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, top_right, 0, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.WEST, type_label, 5, SpringLayout.WEST, this);
        if (top_left.getText().split("[\n]").length >= top_right.getText().split("[\n]").length) {
            layout.putConstraint(SpringLayout.NORTH, type_label, 7, SpringLayout.SOUTH, top_left);
        } else {
            layout.putConstraint(SpringLayout.NORTH, type_label, -3, SpringLayout.SOUTH, top_right);
        }
        layout.putConstraint(SpringLayout.EAST, text_area, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, text_area, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, text_area, 0, SpringLayout.SOUTH, type_label);
        if (text_area.getHeight() > 10) layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, text_area); else {
            layout.putConstraint(SpringLayout.SOUTH, this, main_text.split("<br").length * 10 + 10, SpringLayout.SOUTH, type_label);
        }
    }

    public void setText(LinkedList<HashMap<String, String>> effect, HashSet<String> filter) {
        String text = "", left = "", right = "";
        int requirement_counter = 0;
        try {
            HashMap<String, String> head = effect.remove();
            if (head.get("name").startsWith("(")) title = new TitlePhrase(sys_link.translate(head.get("name").substring(1, head.get("name").length() - 1)), "medium"); else title = new TitlePhrase(head.get("name"), "medium");
            type_label.setText(sys_link.translate(head.get("type")));
            if (head.containsKey("energy")) {
                String energy = head.get("energy");
                if (head.containsKey("maxenergy")) energy += "-" + head.get("maxenergy");
                energy += " " + sys_link.translate("energy");
                if (head.containsKey("intervalenergy")) energy += " " + sys_link.translate("initially");
                left += energy + "\n";
            }
            if (head.containsKey("intervalenergy") && head.containsKey("interval")) {
                left += head.get("intervalenergy") + " " + sys_link.translate("energy") + " " + sys_link.translate("per") + " " + head.get("interval") + " " + sys_link.translate("sec") + "\n";
            } else if (head.containsKey("interval")) {
                left += sys_link.translate("per") + " " + head.get("interval") + " " + sys_link.translate("sec") + "\n";
            }
            if (head.containsKey("maxcharge")) {
                left += sys_link.translate("sec_charge").replace("(1)", head.get("maxcharge"));
                if (head.containsKey("mincharge")) left += " (" + head.get("mincharge") + " " + sys_link.translate("min") + ")";
                left += "\n";
            }
            if (head.containsKey("activation")) {
                left += head.get("activation") + " " + sys_link.translate("activate");
                if (head.containsKey("maxmaintain")) left += " (" + head.get("maxmaintain") + " " + sys_link.translate("max") + ")";
                left += "\n";
            }
            String targetmax = "";
            if (head.containsKey("targetmax")) targetmax = sys_link.translate("targetmax").replace("(1)", head.get("targetmax"));
            if (head.containsKey("target")) right += sys_link.translate("target").replace("(1)", sys_link.translate(head.get("target"))) + " " + targetmax + "\n";
            if (head.containsKey("range")) right += head.get("range") + " " + sys_link.translate("feet");
            if (head.containsKey("lunge")) {
                if (head.containsKey("range")) right += "; ";
                right += sys_link.translate("lunge").replace("(1)", head.get("lunge"));
            }
            if (head.containsKey("sphere")) {
                if (head.containsKey("range") || head.containsKey("lunge")) right += "; ";
                right += sys_link.translate("sphere").replace("(1)", head.get("sphere"));
            }
            if (head.containsKey("cone")) {
                if (head.containsKey("range") || head.containsKey("lunge")) right += "; ";
                right += sys_link.translate("cone").replace("(1)", head.get("cone"));
            }
            if (head.containsKey("cylinder")) {
                if (head.containsKey("range") || head.containsKey("lunge")) right += "; ";
                right += sys_link.translate("cylinder").replace("(1)", head.get("cylinder"));
            }
            if (head.containsKey("range") || head.containsKey("sphere") || head.containsKey("cone") || head.containsKey("cylinder") || head.containsKey("lunge")) right += "\n";
            if (head.containsKey("recharge")) right += head.get("recharge") + " " + sys_link.translate("sec") + " " + sys_link.translate("recharge") + "\n";
            for (HashMap<String, String> item : effect) {
                String target = sys_link.translate(head.get("target"));
                String different_target = "";
                if (item.containsKey("target")) {
                    target = sys_link.translate(item.get("target"));
                    different_target = " " + sys_link.translate("to") + " " + sys_link.translate(item.get("target")) + "";
                }
                if (item.containsKey("upon")) text += sys_link.translate("upon_" + item.get("upon")) + ": ";
                if (item.containsKey("after")) text += sys_link.translate("after_sec").replace("(1)", item.get("after")) + ": ";
                if (item.containsKey("chance")) text += sys_link.translate("proc_chance").replace("(1)", item.get("chance")) + ": ";
                if (item.get("tag").equals("dmg")) {
                    String value = item.get("value");
                    if (item.containsKey("maxvalue")) value += "-" + item.get("maxvalue");
                    if (item.containsKey("old_value")) {
                        value += " (" + item.get("old_value");
                        if (item.containsKey("old_maxvalue")) value += "-" + item.get("old_maxvalue");
                        value += ")";
                    }
                    String dmg_type = sys_link.translate(item.get("type") + "_dmg");
                    dmg_type = markFilterStrings("damage", dmg_type, filter);
                    text += value + " " + dmg_type;
                } else if (item.get("tag").equals("dot")) {
                    String value = item.get("value");
                    if (item.containsKey("maxvalue")) value += "-" + item.get("maxvalue");
                    String dmg_type = sys_link.translate(item.get("type") + "_dmg");
                    dmg_type = markFilterStrings("damage", dmg_type, filter);
                    text += value + " " + dmg_type + " " + sys_link.translate("over_sec").replace("(1)", item.get("time"));
                } else if (item.get("tag").equals("proc")) {
                    String proc_name = sys_link.translate(item.get("name"));
                    proc_name = markFilterStrings("effect", proc_name, filter);
                    text += sys_link.translate("proc").replace("(1)", "<b>" + proc_name + "</b>").replace("(2)", target);
                } else if (item.get("tag").equals("cc")) {
                    if (item.get("type").equals("kb")) text += sys_link.translate("kb").replace("(1)", target).replace("(2)", item.get("distance")); else if (item.get("type").equals("ku")) text += sys_link.translate("ku").replace("(1)", target).replace("(2)", item.get("distance")); else if (item.get("type").equals("kd")) text += sys_link.translate("kd").replace("(1)", target); else if (item.get("type").equals("hold")) {
                        text += item.get("mag") + " " + sys_link.translate("hold");
                    } else if (item.get("type").equals("root")) {
                        if (item.containsKey("mag")) text += item.get("mag") + " " + sys_link.translate("root"); else text += sys_link.translate("root");
                    } else if (item.get("type").equals("repel")) text += item.get("mag") + " " + sys_link.translate("repel"); else if (item.get("type").equals("confuse")) text += item.get("mag") + " " + sys_link.translate("confuse"); else if (item.get("type").equals("placate")) text += sys_link.translate("placate"); else if (item.get("type").equals("kt")) text += sys_link.translate("kt").replace("(1)", item.get("location"));
                } else if (item.get("tag").equals("buff")) {
                    String value = "+" + item.get("value");
                    String characteristic = markFilterStrings("buff", sys_link.translate(item.get("char")), filter);
                    if (item.get("value").startsWith("-")) value = item.get("value");
                    if (item.containsKey("maxvalue")) value += "-" + item.get("maxvalue");
                    text += value + " " + characteristic;
                    if (item.containsKey("over")) text += " " + sys_link.translate("over_sec").replace("(1)", item.get("over"));
                } else if (item.get("tag").equals("debuff")) {
                    String characteristic = markFilterStrings("buff", sys_link.translate(item.get("char")), filter);
                    text += "-" + item.get("value") + " " + characteristic;
                    if (item.containsKey("over")) text += " " + sys_link.translate("over_sec").replace("(1)", item.get("over"));
                } else if (item.get("tag").equals("set")) {
                    String characteristic = markFilterStrings("buff", sys_link.translate(item.get("char")), filter);
                    text += characteristic + " " + sys_link.translate("set_to").replace("(1)", item.get("value"));
                } else if (item.get("tag").equals("mode")) {
                    String action = "enables";
                    String mode_name = sys_link.translate(item.get("name"));
                    mode_name = markFilterStrings("effect", mode_name, filter);
                    if (item.containsKey("status") && item.get("status").equals("off")) action = "disables";
                    text += sys_link.translate(action).replace("(1)", "<b>" + mode_name + "</b>");
                } else if (item.get("tag").equals("reactive")) {
                    String damage = sys_link.translate(item.get("type") + "_dmg");
                    if (item.get("reaction").equals("absorb")) text += sys_link.translate("reactive_absorb").replace("(1)", damage).replace("(2)", item.get("value")); else if (item.get("reaction").equals("damage")) {
                        String reaction_damage = sys_link.translate(item.get("reactiontype") + "_dmg");
                        reaction_damage = markFilterStrings("damage", reaction_damage, filter);
                        text += sys_link.translate("reactive_damage").replace("(1)", damage).replace("(2)", item.get("value")).replace("(3)", reaction_damage);
                    } else {
                        String reaction_name = sys_link.translate(item.get("reaction"));
                        reaction_name = markFilterStrings("effect", reaction_name, filter);
                        text += sys_link.translate("reactive").replace("(1)", damage).replace("(2)", "<b>" + reaction_name + "</b>").replace("(3)", target);
                    }
                } else if (item.get("tag").equals("conversion")) {
                    text += sys_link.translate("conversion").replace("(1)", item.get("initialunit")).replace("(2)", sys_link.translate(item.get("initial"))).replace("(3)", item.get("finalunit")).replace("(4)", sys_link.translate(item.get("final")));
                } else if (item.get("tag").equals("absorb")) {
                    String value = item.get("value");
                    if (item.containsKey("maxvalue")) value += "-" + item.get("maxvalue");
                    String damage = markFilterStrings("damage", sys_link.translate(item.get("type") + "_dmg"), filter);
                    text += sys_link.translate("absorb") + " " + value + " " + damage;
                } else if (item.get("tag").equals("immunity")) {
                    text += sys_link.translate("immunity").replace("(1)", item.get("char"));
                } else if (item.get("tag").equals("summon")) {
                    text += sys_link.translate("summon").replace("(1)", sys_link.translate(item.get("name"))).replace("(2)", "40");
                } else if (item.get("tag").equals("recharge")) {
                    String value = "+" + item.get("value");
                    if (item.get("value").startsWith("-")) value = item.get("value");
                    text += sys_link.translate("recharge_to").replace("(1)", value).replace("(2)", "<b>" + sys_link.translate(item.get("name")) + "</b>");
                } else if (item.get("tag").equals("text")) {
                    text += item.get("text");
                } else {
                    text += "<b>--> unknown effect tag: " + item.get("tag") + " <--</b>";
                }
                if (!item.get("tag").equals("text") && item.containsKey("text")) text += " " + item.get("text");
                if (item.containsKey("max")) text += " " + "(max " + item.get("max") + " times)";
                if (!item.get("tag").equals("proc") && !(item.get("tag").equals("cc") && item.get("type").startsWith("k")) && !item.get("tag").equals("reactive")) text += different_target;
                if (item.containsKey("time") && !item.get("tag").equals("dot")) text += " " + sys_link.translate("for_sec").replace("(1)", item.get("time"));
                if (item.containsKey("requires")) {
                    text += " <span style='color:#fea201'><a style='text-decoration:none' href='(*)#(" + item.get("requires") + ")#" + requirement_counter + "'>(*)</a></span>";
                    requirement_counter++;
                }
                if (item.containsKey("semicolon")) text += "; "; else {
                    if (AppConst.DIST.equals("jar5")) text += "<br><span style='font-size:10%'><br></span>"; else text += "<br/><span style='font-size:10%'><br/></span>";
                }
            }
            if (right.length() > 2) right = right.substring(0, right.length() - 1);
            if (left.length() > 2) left = left.substring(0, left.length() - 1);
            top_left.setText(left);
            top_right.setText(right);
            main_text = "<span style='font-family:Comic Sans MS;font-size:85%;color:white'>" + text + "</span>";
        } catch (Exception e) {
            title = new TitlePhrase("error - please report this");
            top_left.setText("");
            top_right.setText("");
            type_label.setText("");
            main_text = "";
        }
        text_area.setText(main_text);
        redraw();
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            String text = text_area.getText();
            String[] switcher = e.getDescription().toString().split("[#]");
            if (switcher.length > 1) {
                if (main_text.contains("href='" + e.getDescription().toString() + "'>" + switcher[0] + "</a>")) main_text = main_text.replace("href='" + e.getDescription().toString() + "'>" + switcher[0] + "</a>", "href='" + e.getDescription().toString() + "'>" + switcher[1] + "</a>"); else main_text = main_text.replace("href='" + e.getDescription().toString() + "'>" + switcher[1] + "</a>", "href='" + e.getDescription().toString() + "'>" + switcher[0] + "</a>");
                text_area.setText(main_text);
                redraw();
                adv_description.redraw();
            }
        }
    }

    private String markFilterStrings(String type, String text, HashSet<String> filter) {
        int replace_id = 0;
        HashMap<String, String> replace_map = new HashMap<String, String>();
        for (String s : filter) {
            for (String ind_s : s.split("[+]")) {
                if (ind_s.startsWith(type + ":") && !ind_s.substring(type.length() + 1).equals("")) {
                    HashSet<String> set = new HashSet<String>();
                    Pattern p = Pattern.compile("(?i)" + ind_s.substring(type.length() + 1));
                    Matcher m = p.matcher(text);
                    while (m.find()) set.add(m.group());
                    for (String replace : set) {
                        text = text.replace(replace, "####" + replace_id + "####");
                        replace_map.put("####" + replace_id + "####", "<span style='background-color:red'>" + replace + "</span>");
                        replace_id++;
                    }
                }
            }
        }
        for (String key : replace_map.keySet()) {
            text = text.replace(key, replace_map.get(key));
        }
        return text;
    }

    public void setActive(boolean active) {
        is_active = active;
    }

    public boolean isActive() {
        return is_active;
    }
}

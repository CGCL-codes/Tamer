package org.corrib.s3b.sscf.changelog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.corrib.s3b.sscf.manage.XfoafSscfResource;
import org.corrib.s3b.sscf.tools.SscfHelper2;
import org.corrib.s3b.sscf.tools.sscf2js.DirectoryDescription;
import org.foafrealm.manage.Person;
import org.foafrealm.manage.PersonFactory;

/**
 * @author adagze
 *
 */
public class ChangeContent {

    public static String getTitle(int type, int depth, ResourceBundle rb, XfoafSscfResource changeRes) {
        StringBuilder sb = new StringBuilder();
        boolean isDir = true;
        if (changeRes != null && !changeRes.isDirectory()) isDir = false;
        sb.append("feed_title_");
        if (type == 1 || type == ChangeTypes.SUG_ACCEPTED.getChangeType() || type == ChangeTypes.SUG_ADDED.getChangeType()) sb.append("added"); else if (type == 2 || type == ChangeTypes.SUG_REJECTED.getChangeType()) sb.append("removed"); else sb.append("changed");
        if (!isDir) sb.append("_res");
        if (depth > 0 && isDir) sb.append("_sub");
        return rb.getString(sb.toString());
    }

    public static String getMessage(int type, int depth, String changeName, String mainDirName, String changedUri, XfoafSscfResource changedRes, XfoafSscfResource changeRes, ResourceBundle rb) {
        StringBuilder sb = new StringBuilder();
        if (type == 1 || type == ChangeTypes.SUG_ACCEPTED.getChangeType() || type == ChangeTypes.SUG_ADDED.getChangeType()) {
            sb.append(rb.getString("feed_message_added_p1")).append("<br><br>");
            if (depth == 0) sb.append(String.format(rb.getString("feed_message_added_p2"), changeName, mainDirName)); else if (depth == 1) sb.append(String.format(rb.getString("feed_message_added_p2_level"), changeName, mainDirName, changedRes.getLabel())); else sb.append(String.format(rb.getString("feed_message_added_p2_levels"), changeName, mainDirName, depth, changedRes.getLabel()));
            if (changeRes.getIssuedBy() != null) {
                Person p = PersonFactory.findPerson(changeRes.getIssuedBy().getStringURI());
                if (p != null) {
                    if (p.getName() != null && !"".equals(p.getName())) {
                        sb.append("<BR><BR>");
                        sb.append(String.format(rb.getString("feed_message_added_owner"), p.getName()));
                    } else {
                        String name = p.getGivenname() + " " + p.getFamily_name();
                        if (!" ".equals(name)) {
                            sb.append("<BR><br>");
                            sb.append(String.format(rb.getString("feed_message_added_owner"), name));
                        }
                    }
                }
            }
            sb.append("<Br><br>");
            StringBuilder tags = new StringBuilder();
            StringBuilder tax = new StringBuilder();
            List<String> tagsTab = changeRes.getTags();
            if (tagsTab != null && tagsTab.size() > 0) {
                for (String tag : tagsTab) {
                    tags.append(tag);
                    tags.append(", ");
                }
                if (tags.length() > 2) tags.delete(tags.length() - 2, tags.length());
            }
            if (changeRes.isDirectory()) {
                DirectoryDescription dd = SscfHelper2.getInstance().getDirectoryDescriptionObject(changedRes.getStringURI());
                for (String taxonomy : dd.getTaxonomies().keySet()) {
                    Map<String, String> list = dd.getTaxonomyValues().get(taxonomy);
                    if (list != null && !list.isEmpty()) {
                        tax.append("<li>");
                        tax.append("<B>").append(taxonomy).append(": </B>");
                        for (String item : list.keySet()) {
                            tax.append(list.get(item)).append(", ");
                        }
                        if (tax.length() > 2) tax.delete(tax.length() - 2, tax.length());
                        tax.append("</li>");
                    }
                }
            }
            if (tags.length() > 0 || tax.length() > 0) {
                sb.append(rb.getString("feed_message_added_annotations"));
                sb.append("<ul>");
                if (tags.length() > 0) {
                    sb.append("<li><b>").append(rb.getString("feed_message_added_annotations_tags")).append(": </b>");
                    sb.append(tags).append("</li>");
                }
                sb.append(tax);
                sb.append("</ul>");
            }
            sb.append("<Br><br>");
            String resUri = changeRes.getURI().toString();
            if (changeRes.isDirectory()) sb.append(String.format(rb.getString("feed_message_added_link"), resUri)); else sb.append(String.format(rb.getString("feed_message_added_link"), resUri.substring(0, resUri.lastIndexOf("__"))));
        } else if (type == 2 || type == ChangeTypes.SUG_REJECTED.getChangeType()) {
            sb.append(String.format(rb.getString("feed_message_removed_p1"), changeName, mainDirName)).append("<br><Br>");
            if (depth == 0) sb.append(String.format(rb.getString("feed_message_removed_p2"), changeName, mainDirName)); else if (depth == 1) sb.append(String.format(rb.getString("feed_message_removed_p2_level"), changeName, mainDirName, changedRes.getLabel())); else sb.append(String.format(rb.getString("feed_message_removed_p2_levels"), changeName, mainDirName, depth, changedRes.getLabel()));
            sb.append("<Br><br>");
        } else {
            String chName = changedRes.getLabel();
            sb.append(String.format(rb.getString("feed_message_changed_p1"), chName)).append("<br><Br>");
            if (depth > 0) {
                if (depth == 1) sb.append(String.format(rb.getString("feed_message_changed_p2"), chName, mainDirName)); else if (depth == 2) sb.append(String.format(rb.getString("feed_message_changed_p2_level"), chName, mainDirName)); else sb.append(String.format(rb.getString("feed_message_changed_p2_levels"), chName, mainDirName, depth));
                if (changedRes.getIssuedBy() != null) {
                    Person p = PersonFactory.findPerson(changedRes.getIssuedBy().getStringURI());
                    if (p != null) {
                        if (p.getName() != null && !"".equals(p.getName())) {
                            sb.append("<BR><br>");
                            sb.append(String.format(rb.getString("feed_message_changed_owner"), p.getName()));
                        } else {
                            String name = p.getGivenname() + " " + p.getFamily_name();
                            if (!" ".equals(name)) {
                                sb.append("<BR><br>");
                                sb.append(String.format(rb.getString("feed_message_changed_owner"), name));
                            }
                        }
                    }
                }
                sb.append("<Br><br>");
                if (changeName != null && !"".equals(changeName)) {
                    sb.append(rb.getString("feed_message_changed_changeslist"));
                    sb.append("<ul>");
                    Map<String, String> changesMap = new HashMap<String, String>();
                    String[] changes = changeName.split("\\|\\|");
                    for (String change : changes) {
                        if (!"".equals(change.trim())) {
                            String changeId = change.substring(0, change.indexOf(':'));
                            changesMap.put(changeId, change.substring(change.indexOf(':') + 1));
                        }
                    }
                    List<ChangeTypes> subChanges = ChangeTypes.getChangesList(type);
                    for (ChangeTypes subChange : subChanges) {
                        String change = changesMap.get(String.valueOf(subChange.getChangeType()));
                        String[] befaft = null;
                        if (change != null) befaft = change.split("::", 2);
                        sb.append("<li>");
                        if (befaft != null && befaft[1] != null && !"".equals(befaft[1])) sb.append(String.format(rb.getString("feed_message_changed_changelist_" + subChange.getShortName()), befaft[1]));
                        if (befaft != null && befaft[0] != null && !"".equals(befaft[0])) sb.append(String.format(rb.getString("feed_message_changed_changelist_" + subChange.getShortName() + "_old"), befaft[0]));
                        sb.append("</li>");
                    }
                    sb.append("</ul><br><br>");
                }
            }
            sb.append(String.format(rb.getString("feed_message_changed_link"), changedUri));
        }
        return sb.toString();
    }
}

package org.maveryx.jviewer.guiObjectRecognition;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.accessibility.AccessibleRole;
import org.maveryx.jviewer.viewMap.AccessibilityViewObject;
import org.maveryx.jviewer.viewMap.FindFilter;
import org.maveryx.jviewer.viewMap.JViewerMap;

/**
 * Definisce un insieme di criteri per il riconoscimento di ua finestra informativa di tipo "WinError".
 * @author Giacomo Perreca
 */
public class WinErrorRecognitionCriteriaSet extends WinRecognitionCriteriaSet {

    /**
	 * Costruisce un in sieme di criteri per il riconoscimento di ua finestra informativa di tipo "WinError".
	 * @param termSetFilePath Percorso del documento contenente il vocabolario dei termini.
	 */
    public WinErrorRecognitionCriteriaSet(String termSetFilePath) {
        super(termSetFilePath);
        RecognitionCriteria rc1, rc2, rc3;
        rc1 = new RecognitionCriteria("criteria_1") {

            protected float evaluationCriteria() {
                JViewerMap avMap = getMap();
                AccessibilityViewObject dialog = avMap.topLevelWindow();
                if (dialog == null) return 0.0f;
                if (dialog.role().compareTo(AccessibleRole.DIALOG.toDisplayString(Locale.ENGLISH)) != 0) return 0.0f;
                env.put("dialog", dialog);
                return 1.0f;
            }
        };
        addCriteria(rc1);
        rc2 = new RecognitionCriteria("criteria_1.1") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                if (dialog == null) return 0.0f;
                FindFilter labelFilter = new FindFilter() {

                    public boolean accept(AccessibilityViewObject avo) {
                        if (avo == null) return false;
                        if (avo.role().compareToIgnoreCase(AccessibleRole.LABEL.toDisplayString(Locale.ENGLISH)) != 0) return false;
                        if (avo.name() == null || avo.name().length() == 0) return false;
                        return true;
                    }
                };
                AccessibilityViewObject[] labels = dialog.find(labelFilter);
                if (labels == null || labels.length == 0) return 0.0f;
                env.put("labels", labels);
                return 1.0f;
            }
        };
        rc1.addSubCriteria(rc2);
        rc3 = new RecognitionCriteria("criteria_1.1.1") {

            protected float evaluationCriteria() {
                AccessibilityViewObject[] labels = (AccessibilityViewObject[]) env.get("labels");
                String functionality = ((String) env.get("functionality")).toLowerCase();
                if (functionality == null || functionality.length() == 0) return 0.0f;
                for (int i = 0; i < labels.length; i++) {
                    String labelText = labels[i].name().toLowerCase();
                    if (labelText.contains(functionality)) return 1.0f;
                }
                return 0.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc3 = new RecognitionCriteria("criteria_1.1.2") {

            protected float evaluationCriteria() {
                AccessibilityViewObject[] labels = (AccessibilityViewObject[]) env.get("labels");
                Set termSet = (Set) env.get("notSuccessTermSet");
                Iterator iter = termSet.iterator();
                for (int i = 0; i < labels.length; i++) {
                    String labelText = labels[i].name().toLowerCase();
                    while (iter.hasNext()) {
                        String term = ((String) iter.next()).toLowerCase();
                        if (labelText.contains(term)) return 1.0f;
                    }
                }
                return 0.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc3 = new RecognitionCriteria("criteria_1.1.3") {

            protected float evaluationCriteria() {
                AccessibilityViewObject[] labels = (AccessibilityViewObject[]) env.get("labels");
                Set termSet = (Set) env.get("successTermSet");
                Iterator iter = termSet.iterator();
                for (int i = 0; i < labels.length; i++) {
                    String labelText = labels[i].name().toLowerCase();
                    while (iter.hasNext()) {
                        String term = ((String) iter.next()).toLowerCase();
                        if (labelText.contains(term)) return 0.0f;
                    }
                }
                return 1.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc2 = new RecognitionCriteria("criteria_1.2") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                if (dialog == null) return 0.0f;
                FindFilter labelFilter = new FindFilter() {

                    public boolean accept(AccessibilityViewObject avo) {
                        if (avo == null) return false;
                        if (avo.role().compareToIgnoreCase(AccessibleRole.PUSH_BUTTON.toDisplayString(Locale.ENGLISH)) != 0) return false;
                        if (avo.name() == null || avo.name().length() == 0) return false;
                        return true;
                    }
                };
                AccessibilityViewObject[] button = dialog.find(labelFilter);
                if (button == null || button.length == 0 || button.length > 1) return 0.0f;
                env.put("button", button[0]);
                return 1;
            }
        };
        rc1.addSubCriteria(rc2);
        rc3 = new RecognitionCriteria("criteria_1.2.1") {

            protected float evaluationCriteria() {
                AccessibilityViewObject button = (AccessibilityViewObject) env.get("button");
                Set termSet = (Set) env.get("ackTermSet");
                Iterator iter = termSet.iterator();
                String buttonText = button.name().toLowerCase();
                while (iter.hasNext()) {
                    String term = ((String) iter.next()).toLowerCase();
                    if (buttonText.contains(term)) return 1.0f;
                }
                return 0.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc2 = new RecognitionCriteria("criteria_1.3") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                if (dialog == null) return 0.0f;
                FindFilter labelFilter = new FindFilter() {

                    public boolean accept(AccessibilityViewObject avo) {
                        if (avo == null) return false;
                        if (avo.role().compareToIgnoreCase(AccessibleRole.LABEL.toDisplayString(Locale.ENGLISH)) != 0) return false;
                        if (avo.iconDescriptions() == null || avo.iconDescriptions().iconCount() == 0) return false;
                        return true;
                    }
                };
                AccessibilityViewObject[] icon = dialog.find(labelFilter);
                if (icon == null || icon.length > 1) return 0.0f;
                env.put("icon", icon);
                return 1.0f;
            }
        };
        rc1.addSubCriteria(rc2);
        rc2 = new RecognitionCriteria("criteria_1.4") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                if (dialog == null) return 0.0f;
                if (dialog.name() == null || dialog.name().length() == 0) return 0.0f;
                return 1.0f;
            }
        };
        rc1.addSubCriteria(rc2);
        rc3 = new RecognitionCriteria("criteria_1.4.1") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                String functionality = (String) env.get("functionality");
                if (functionality == null || functionality.length() == 0) return 0.0f;
                String title = dialog.name();
                if (title.contains(functionality)) return 1.0f;
                return 0.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc3 = new RecognitionCriteria("criteria_1.4.2") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                Set termSet = (Set) env.get("notSuccessTermSet");
                Iterator iter = termSet.iterator();
                String title = dialog.name().toLowerCase();
                while (iter.hasNext()) {
                    String term = ((String) iter.next()).toLowerCase();
                    System.out.println("title=" + title + " term=" + term + ": " + title.contains(term));
                    if (title.contains(term)) return 1.0f;
                }
                return 0.0f;
            }
        };
        rc2.addSubCriteria(rc3);
        rc3 = new RecognitionCriteria("criteria_1.4.3") {

            protected float evaluationCriteria() {
                AccessibilityViewObject dialog = (AccessibilityViewObject) env.get("dialog");
                Set termSet = (Set) env.get("successTermSet");
                System.out.println("succ. term set count: " + termSet.size());
                Iterator iter = termSet.iterator();
                String title = dialog.name().toLowerCase();
                while (iter.hasNext()) {
                    String term = ((String) iter.next()).toLowerCase();
                    System.out.println("title=" + title + " term=" + term + ": " + title.contains(term));
                    if (title.contains(term)) return 0.0f;
                }
                return 1.0f;
            }
        };
        rc2.addSubCriteria(rc3);
    }
}

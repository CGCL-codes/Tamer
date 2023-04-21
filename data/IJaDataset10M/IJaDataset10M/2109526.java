package strudle;

public class SelectionSet {

    AudioSelection[] selections;

    public SelectionSet() {
        selections = null;
    }

    public SelectionSet(int c) {
        selections = new AudioSelection[c];
    }

    int isIn(Trial trial) {
        if (selections != null) {
            for (int i = 0; i < selections.length; i++) {
                if (selections[i].trial[0].equals(trial)) return i;
            }
        }
        return -1;
    }

    AudioSelection getSelection(int c) {
        return selections[c];
    }

    void addSelection(AudioSelection sel) {
        if (selections != null) {
            AudioSelection[] tmp = new AudioSelection[selections.length + 1];
            for (int i = 0; i < selections.length; i++) {
                tmp[i] = selections[i];
            }
            selections = tmp;
        } else {
            selections = new AudioSelection[1];
            System.out.println("creato array selezione");
        }
        selections[selections.length - 1] = sel;
    }

    void setSelection(int index, AudioSelection sel) {
        selections[index] = sel;
    }

    int length() {
        if (selections != null) return selections.length; else return 0;
    }

    int[] getSameSelection(int c) {
        int[] res = null;
        if (selections != null) for (int i = 0; i < selections.length; i++) {
            if (i != c) {
                if (selections[i].start == selections[c].start && selections[i].fine == selections[c].fine) {
                    if (res != null) {
                        int[] tmp = new int[res.length + 1];
                        for (int j = 0; j < res.length; j++) {
                            tmp[j] = res[j];
                        }
                        tmp[res.length] = i;
                        res = tmp;
                    } else {
                        res = new int[1];
                        res[0] = i;
                    }
                }
            }
        }
        return res;
    }

    AudioSelection[] getProve4Recog() {
        int[] notToSelect = new int[selections.length];
        for (int i = 0; i < notToSelect.length; i++) {
            notToSelect[i] = -1;
        }
        AudioSelection[] tmp = null;
        for (int i = 0; i < selections.length; i++) {
            if (notToSelect[i] == -1) {
                if (tmp != null) {
                    AudioSelection[] tmp1 = new AudioSelection[tmp.length + 1];
                    for (int j = 0; j < tmp.length; j++) {
                        tmp1[j] = tmp[j];
                    }
                    tmp = tmp1;
                } else {
                    tmp = new AudioSelection[1];
                }
                tmp[tmp.length - 1] = selections[i];
                int[] sameSel = getSameSelection(i);
                if (sameSel != null) {
                    for (int j = 0; j < sameSel.length; j++) {
                        System.out.println(sameSel[j]);
                        tmp[tmp.length - 1].addProva(selections[sameSel[j]].trial[0]);
                        notToSelect[sameSel[j]] = i;
                    }
                }
            }
        }
        return tmp;
    }
}

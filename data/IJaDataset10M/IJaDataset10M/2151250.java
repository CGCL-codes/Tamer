package jmri.jmrit.vsdecoder;

class NotchTransition extends SoundBite {

    private int prev_notch;

    private int next_notch;

    public NotchTransition(String name) {
        super(name);
        prev_notch = 0;
        next_notch = 0;
        length = 0;
    }

    public NotchTransition(VSDFile vf, String filename, String sname, String uname) {
        super(vf, filename, sname, uname);
        prev_notch = 0;
        next_notch = 0;
        this.setLength();
    }

    public int getPrevNotch() {
        return (prev_notch);
    }

    public int getNextNotch() {
        return (next_notch);
    }

    public void setPrevNotch(int p) {
        prev_notch = p;
    }

    public void setNextNotch(int p) {
        next_notch = p;
    }
}

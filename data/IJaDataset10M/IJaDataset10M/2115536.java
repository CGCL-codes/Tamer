package vidis.modules.bullyElectionAlgorithm;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class BullyElectionAlgorithmNode extends AUserNode {

    public String bully = null;

    @Display(name = "checkTimeout")
    public int checkTimeout = -1;

    @Display(name = "restartTimeout")
    public int startTimeout = -1;

    @Display(name = "enabled")
    public boolean enabled = true;

    @Display(name = "gotBully")
    public boolean gotBully() {
        return bully != null;
    }

    @Display(name = "bully")
    public String getBully() {
        return bully;
    }

    private boolean amIBiggerBully(String me, String other) {
        return me.compareTo(other) > 0;
    }

    @Override
    public void init() {
    }

    public void execute() {
        if (checkTimeout > 0) {
            checkTimeout--;
        } else if (checkTimeout == 0) {
            checkTimeout = -1;
            restart();
            startTimeout = 60;
        } else {
        }
        if (startTimeout > 0) {
            startTimeout--;
        } else if (startTimeout == 0) {
            startTimeout = -1;
            start();
        }
    }

    @Display(name = "Reset election!")
    public void restart() {
        bully = null;
        for (IUserLink l : getConnectedLinks()) {
            sendMy(new ElectionRestartPacket(getBullyId()), l, null);
        }
    }

    @Display(name = "Start election!")
    public void start() {
        this.bully = getBullyId();
        for (IUserLink l : getConnectedLinks()) {
            sendMy(new ElectionPacket(getBullyId()), l, null);
        }
    }

    @Display(name = "Check election!")
    public void check() {
        if (gotBully()) {
            checkTimeout = 100;
            for (IUserLink l : getConnectedLinks()) {
                sendMy(new PingPacket(getBully(), getBullyId()), l, null);
            }
        } else {
            start();
        }
    }

    @Display(name = "Enable/Disable")
    public void toggleEnabled() {
        enabled = !enabled;
    }

    private void sendMy(ABullyPacket p, IUserLink l, Integer hops) {
        long wait = (int) (Math.random() * 2 + 0);
        if (enabled == false) {
            return;
        }
        if (hops != null) {
            p.setHops(hops - 1);
        }
        if (p.getHops() > 0) send(p, l, wait);
    }

    private void receive(PongPacket p) {
        if (p.getSenderId().equals(getBullyId())) {
            checkTimeout = -1;
        } else {
            for (IUserLink l : getConnectedLinks()) {
                if (!l.equals(p.getLinkToSource())) {
                    sendMy(new PongPacket(p.getBullyId(), p.getSenderId()), l, p.getHops());
                }
            }
        }
    }

    private void receive(PingPacket p) {
        if (!gotBully() || !p.getBullyId().equals(getBully())) {
            restart();
        } else {
            if (getBullyId().equals(p.getBullyId())) {
                sendMy(new PongPacket(getBully(), p.getSenderId()), p.getLinkToSource(), p.getMaxHops() - p.getHops());
            } else if (!p.getSenderId().equals(getBullyId())) {
                for (IUserLink l : getConnectedLinks()) {
                    if (!p.getLinkToSource().equals(l)) {
                        sendMy(new PingPacket(p.getBullyId(), p.getSenderId()), l, p.getHops());
                    }
                }
            }
        }
    }

    private void receive(ElectionRestartPacket p) {
        if (gotBully()) {
            for (IUserLink l : getConnectedLinks()) {
                if (!l.equals(p.getLinkToSource())) {
                    sendMy(new ElectionRestartPacket(getBullyId()), l, p.getHops());
                }
            }
        }
        bully = null;
    }

    private void propagateBully(String bully, IUserLink notToThisOne, Integer hops) {
        for (IUserLink l : getConnectedLinks()) {
            if (notToThisOne != null && !l.equals(notToThisOne)) {
                sendMy(new ElectionPacket(bully), l, hops);
            }
        }
    }

    private void receive(ElectionPacket p) {
        if (gotBully()) {
            if (p.getBullyId().equals(getBully())) {
            } else {
                if (amIBiggerBully(getBully(), p.getBullyId())) {
                    sendMy(new ElectionPacket(getBully()), p.getLinkToSource(), p.getHops());
                } else {
                    bully = p.getBullyId();
                    propagateBully(bully, p.getLinkToSource(), p.getHops());
                }
            }
        } else {
            if (amIBiggerBully(getBullyId(), p.getBullyId())) {
                sendMy(new ElectionPacket(getBullyId()), p.getLinkToSource(), p.getHops());
                bully = getBullyId();
                propagateBully(bully, p.getLinkToSource(), p.getHops());
            } else {
                bully = p.getBullyId();
                propagateBully(bully, p.getLinkToSource(), p.getHops());
            }
        }
    }

    public void receive(IUserPacket packet) {
        if (enabled == false) {
            return;
        }
        if (packet instanceof ElectionRestartPacket) {
            receive((ElectionRestartPacket) packet);
        } else if (packet instanceof PingPacket) {
            receive((PingPacket) packet);
        } else if (packet instanceof ElectionPacket) {
            receive((ElectionPacket) packet);
        } else if (packet instanceof PongPacket) {
            receive((PongPacket) packet);
        } else {
        }
    }

    public String getBullyId() {
        return getId();
    }

    public boolean amIBully() {
        return gotBully() && bully.equals(getBullyId());
    }

    @DisplayColor
    public ColorType getNodeColor() {
        if (!enabled) return ColorType.RED; else if (checkTimeout > 0) return ColorType.ORANGE_LIGHT; else if (startTimeout > 0) return ColorType.ORANGE; else if (amIBully()) return ColorType.GREEN; else return ColorType.GREY;
    }

    @Display(name = "header1")
    public String getHeader1() {
        String out = getBullyId();
        return out;
    }

    @Display(name = "header2")
    public String getHeader2() {
        String out = "";
        if (gotBully()) {
            if (bully.equals(getBullyId())) out += "Bully=ME!"; else {
                out += "Bully=" + bully;
            }
        } else {
            out += "Bully=???";
        }
        if (checkTimeout > -1) {
            out += "-CHECK=" + checkTimeout;
        }
        if (startTimeout > -1) {
            out += "-STARTIN=" + startTimeout;
        }
        if (!enabled) out += "-DISABLED";
        return out;
    }
}

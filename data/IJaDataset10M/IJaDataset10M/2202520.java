package sf2.view.impl.paxos.vc;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sf2.core.Event;
import sf2.core.Tag;
import sf2.view.impl.paxos.HandlerBase;
import sf2.view.impl.paxos.PaxosShare;
import sf2.view.impl.paxos.PaxosState;
import sf2.view.impl.paxos.PaxosView;
import sf2.view.impl.paxos.PaxosViewStamp;
import sf2.view.impl.paxos.vc.msg.NewView;
import sf2.view.impl.paxos.vc.msg.VCAccept;
import sf2.view.impl.paxos.vc.msg.ViewChange;
import sf2.view.impl.paxos.vc.msg.ViewChangeTimeout;

public class HandlerVCAccept extends HandlerBase {

    public HandlerVCAccept(PaxosShare shared) {
        super(shared);
    }

    public void handle(PaxosState paxos, Event event) {
        if (event instanceof VCAccept) {
            handleAccept(paxos, (VCAccept) event);
        } else if (event instanceof ViewChangeTimeout) {
            handleTimeout(paxos, (ViewChangeTimeout) event);
        }
    }

    protected void handleAccept(PaxosState paxos, VCAccept ack) {
        logging.debug(LOG_NAME, localAddr + " <- " + ack.getSender() + " view-change ack  latest=" + ack.getLatest() + ", ackview=" + ack.getNewView());
        paxos.acks.add(ack);
        PaxosView[] newView = ack.getNewView();
        if (newView != null && newView.length > 0) foundConcurrent(paxos, ack);
        if (paxos.isConcurrent) {
            handleConcurrent(paxos, ack);
            return;
        }
        if (paxos.acks.size() == paxos.candidates.size()) {
            if (paxos.timeoutTask != null) {
                paxos.timeoutTask.cancel();
                paxos.timeoutTask = null;
            }
            logging.debug(LOG_NAME, localAddr + "  all cohorts reply num=" + paxos.acks.size());
            formNewView(paxos);
            paxos.acks.clear();
        }
    }

    protected void foundConcurrent(PaxosState paxos, VCAccept ack) {
        paxos.isConcurrent = true;
        boolean updated = false;
        for (PaxosView v : ack.getNewView()) {
            if (paxos.vDash == null || paxos.vDash.getViewId().lt(v.getViewId())) {
                paxos.vDash = v;
                updated = true;
            }
        }
        if (!updated) return;
        logging.debug(LOG_NAME, localAddr + " -------------- CONCURRENT MODE --------------");
        logging.debug(LOG_NAME, localAddr + " accept non-NULL accept.newView size=" + ack.getNewView().length);
        logging.debug(LOG_NAME, localAddr + " update V'  V'=" + paxos.vDash);
        ViewChange vcReq = new ViewChange(paxos.key, paxos.view, paxos.highest, localAddr);
        if (!paxos.candidates.contains(paxos.vDash.getPrimary())) {
            logging.debug(LOG_NAME, localAddr + " -> " + paxos.vDash.getPrimary() + " additional view-change  highest=" + paxos.highest);
            client.send(paxos.vDash.getPrimary(), Tag.PAXOS, vcReq, queue);
        }
        for (InetAddress b : paxos.vDash.getBackups()) {
            if (!paxos.candidates.contains(b)) {
                logging.debug(LOG_NAME, localAddr + " -> " + b + " additional view-change highest=" + paxos.highest);
                client.send(b, Tag.PAXOS, vcReq, queue);
            }
        }
    }

    protected void handleConcurrent(PaxosState paxos, VCAccept ack) {
        if (reachesMajority(paxos.acks, paxos.view) && reachesMajority(paxos.acks, paxos.vDash)) {
            paxos.timeoutTask.cancel();
            formNewViewFromDash(paxos);
        }
    }

    protected void formNewViewFromDash(PaxosState paxos) {
        PaxosViewStamp latestAll = null;
        logging.debug(LOG_NAME, localAddr + " (CONCURRENT) reached majority in V_old and V' num=" + paxos.acks.size());
        if (containes(paxos.acks, paxos.vDash.getPrimary())) {
            paxos.newView = new PaxosView(paxos.highest, paxos.vDash.getPrimary(), paxos.vDash.getBackups());
            logging.debug(LOG_NAME, localAddr + " (CONCURRENT) newView=" + paxos.newView);
            for (VCAccept a : paxos.acks) {
                if (a.getSender().equals(paxos.newView.getPrimary())) {
                    latestAll = a.getLatest();
                    break;
                }
            }
        } else {
            PaxosViewStamp latest = null;
            VCAccept latestOne = null;
            for (VCAccept a : paxos.acks) {
                InetAddress target = a.getSender();
                if (containes(paxos.vDash, target) && (latest == null || latest.lt(a.getLatest()))) {
                    latest = a.getLatest();
                    latestOne = a;
                }
            }
            latestAll = latest;
            InetAddress primary = latestOne.getSender();
            Set<InetAddress> backups = new HashSet<InetAddress>();
            backups.add(paxos.vDash.getPrimary());
            backups.addAll(paxos.vDash.getBackups());
            paxos.newView = new PaxosView(paxos.highest, primary, backups);
            logging.debug(LOG_NAME, localAddr + " (CONCURRENT) newView=" + paxos.newView);
        }
        logging.debug(LOG_NAME, localAddr + " (CONCURRENT) latest=" + latestAll);
        NewView nv = new NewView(paxos.key, localAddr, latestAll, paxos.newView);
        logging.debug(LOG_NAME, localAddr + " (CONCURRENT) -> " + paxos.newView.getPrimary() + " (3) new view");
        client.send(paxos.newView.getPrimary(), Tag.PAXOS, nv, queue);
        for (InetAddress b : paxos.newView.getBackups()) {
            logging.debug(LOG_NAME, localAddr + " (CONCURRENT) -> " + b + " (3) new view");
            client.send(b, Tag.PAXOS, nv, queue);
        }
    }

    protected boolean reachesMajority(List<VCAccept> acks, PaxosView view) {
        int majority = (1 + view.getBackups().size()) / 2 + 1;
        int count = 0;
        for (VCAccept a : acks) {
            InetAddress target = a.getSender();
            if (target.equals(view.getPrimary()) || view.getBackups().contains(target)) count++;
        }
        return count >= majority;
    }

    protected boolean containes(PaxosView view, InetAddress target) {
        if (view.getPrimary().equals(target)) return true;
        if (view.getBackups().contains(target)) return true;
        return false;
    }

    protected boolean containes(InetAddress[] arr, InetAddress target) {
        for (InetAddress a : arr) {
            if (a.equals(target)) return true;
        }
        return false;
    }

    protected void handleTimeout(PaxosState paxos, ViewChangeTimeout timeout) {
        logging.debug(LOG_NAME, "Paxos VC timeout");
        formNewView(paxos);
    }

    protected void formNewView(PaxosState paxos) {
        logging.debug(LOG_NAME, localAddr + " ---------- 2ND ROUND ----------");
        InetAddress oldPrimary = paxos.view.getPrimary();
        List<VCAccept> newMembers = new ArrayList<VCAccept>();
        for (VCAccept a : paxos.acks) {
            if (a.includeMe()) newMembers.add(a);
        }
        if (!containes(newMembers, localAddr)) {
            PaxosView[] accepted = !paxos.acceptedView.isEmpty() ? (PaxosView[]) paxos.acceptedView.toArray() : null;
            newMembers.add(new VCAccept(paxos.key, localAddr, true, paxos.persistLog.getLatest(), accepted));
        }
        if (!containes(newMembers, oldPrimary)) {
            if (containes(paxos.acks, oldPrimary)) {
                newMembers.add(paxos.acks.get(paxos.acks.indexOf(oldPrimary)));
            } else {
                findNewCandidates(paxos, newMembers);
            }
        }
        InetAddress newPrimary = null;
        if (paxos.preferredPrimary == null) {
            if (containes(newMembers, oldPrimary)) newPrimary = oldPrimary; else {
                VCAccept latestOne = null;
                PaxosViewStamp latest = null;
                for (VCAccept a : newMembers) {
                    if (latest == null || a.getLatest().gt(latest)) {
                        latest = a.getLatest();
                        latestOne = a;
                    }
                }
                newPrimary = latestOne.getSender();
            }
        } else {
            if (containes(newMembers, paxos.preferredPrimary)) {
                newPrimary = paxos.preferredPrimary;
            } else {
                logging.warning(LOG_NAME, localAddr + " not found preferredPrimary in newMembers. We choose it automatically...");
                if (containes(newMembers, oldPrimary)) newPrimary = oldPrimary; else {
                    VCAccept latestOne = null;
                    PaxosViewStamp latest = null;
                    for (VCAccept a : newMembers) {
                        if (latest == null || a.getLatest().gt(latest)) {
                            latest = a.getLatest();
                            latestOne = a;
                        }
                    }
                    newPrimary = latestOne.getSender();
                }
            }
        }
        logging.debug(LOG_NAME, localAddr + " newPrimary=" + newPrimary + ", latest=" + paxos.persistLog.getLatest());
        Set<InetAddress> newBackups = new HashSet<InetAddress>();
        for (VCAccept a : newMembers) newBackups.add(a.getSender());
        newBackups.remove(newPrimary);
        PaxosViewStamp allLatest = null;
        for (VCAccept a : newMembers) {
            if (allLatest == null || allLatest.lt(a.getLatest())) {
                allLatest = a.getLatest();
            }
        }
        paxos.reachedMajority = false;
        paxos.newView = new PaxosView(paxos.highest, newPrimary, newBackups);
        NewView nv = new NewView(paxos.key, localAddr, allLatest, paxos.newView);
        for (VCAccept m : newMembers) {
            logging.debug(LOG_NAME, localAddr + " -> " + m.getSender() + " (3) new view");
            client.send(m.getSender(), Tag.PAXOS, nv, queue);
        }
        if (!containes(newMembers, paxos.view.getPrimary())) {
            logging.debug(LOG_NAME, localAddr + " -> " + paxos.view.getPrimary() + " (3) new view");
            client.send(paxos.view.getPrimary(), Tag.PAXOS, nv, queue);
        }
        for (InetAddress b : paxos.view.getBackups()) {
            if (!containes(newMembers, b)) {
                logging.debug(LOG_NAME, localAddr + " -> " + b + " (3) new view");
                client.send(b, Tag.PAXOS, nv, queue);
            }
        }
    }

    protected void findNewCandidates(PaxosState paxos, List<VCAccept> newMembers) {
        logging.debug(LOG_NAME, localAddr + " findNewCandidates()");
        int majority = (1 + paxos.view.getBackups().size()) / 2 + 1;
        int count = 0;
        LinkedList<VCAccept> notMembers = new LinkedList<VCAccept>(paxos.acks);
        for (Iterator<VCAccept> i = notMembers.iterator(); i.hasNext(); ) {
            VCAccept ack = i.next();
            if (newMembers.contains(ack)) {
                i.remove();
                count++;
            }
        }
        logging.debug(LOG_NAME, localAddr + " newMembers(1st)=" + newMembers + ", count=" + count);
        while (count < majority) {
            if (notMembers.size() == 0) {
                logging.severe(LOG_NAME, localAddr + " UNRECOVERBLE findNewCandites()");
            }
            PaxosViewStamp latest = null;
            VCAccept latestOne = null;
            for (VCAccept ack : notMembers) {
                if (latest == null || latest.lt(ack.getLatest())) {
                    latest = ack.getLatest();
                    latestOne = ack;
                }
            }
            newMembers.add(latestOne);
            count++;
            logging.debug(LOG_NAME, localAddr + " newMembers(2nd)=" + newMembers);
        }
    }

    protected boolean containes(List<VCAccept> list, InetAddress target) {
        for (VCAccept a : list) {
            if (a.getSender().equals(target)) return true;
        }
        return false;
    }
}

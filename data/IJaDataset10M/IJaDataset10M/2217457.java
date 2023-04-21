package org.jactr.core.module.declarative.four.associative;

import javolution.util.FastList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.chunk.event.ChunkEvent;
import org.jactr.core.chunk.event.ChunkListenerAdaptor;
import org.jactr.core.chunk.four.ISubsymbolicChunk4;
import org.jactr.core.chunk.four.Link4;
import org.jactr.core.chunk.link.IAssociativeLink;

/**
 * Chunk listener that handles associative links. This does three things. 1) it
 * creates (or removes) associative links between this chunk (i) and any chunks
 * it has as slot values (j). 2) it handles the associative links when a chunk
 * is merged. 3) updates the statistics used for Sji calculation on merging
 * 
 * @author developer
 */
public class ChunkListener extends ChunkListenerAdaptor {

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(ChunkListener.class);

    private final DefaultAssociativeLinkageSystem4 _linkageSystem;

    public ChunkListener(DefaultAssociativeLinkageSystem4 linkageSystem) {
        _linkageSystem = linkageSystem;
    }

    @Override
    public void chunkEncoded(ChunkEvent ce) {
        ce.getSource().removeListener(this);
    }

    /**
   * handles the updating of associative links, The updating is done here since
   * the listener is removed after encoding, so the master chunk will not have
   * this listener attached
   * 
   * @param event
   * @see org.jactr.core.chunk.event.ChunkListenerAdaptor#mergingInto(org.jactr.core.chunk.event.ChunkEvent)
   */
    @Override
    public void mergingInto(ChunkEvent event) {
        IChunk self = event.getSource();
        IChunk master = event.getChunk();
        if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Merging %s into %s", self, master));
        ISubsymbolicChunk4 selfSSC = (ISubsymbolicChunk4) self.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class);
        ISubsymbolicChunk4 masterSSC = (ISubsymbolicChunk4) master.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class);
        masterSSC.setTimesInContext(masterSSC.getTimesInContext() + selfSSC.getTimesInContext());
        masterSSC.setTimesNeeded(masterSSC.getTimesNeeded() + selfSSC.getTimesNeeded());
        FastList<IAssociativeLink> links = FastList.newInstance();
        selfSSC.getIAssociations(links);
        for (IAssociativeLink iLink : links) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Testing old iLink %s", iLink));
            IChunk iChunk = iLink.getIChunk();
            if (iChunk.equals(self)) continue;
            Link4 masterLink = (Link4) masterSSC.getIAssociation(iChunk);
            if (masterLink != null) {
                Link4 l = (Link4) iLink;
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s (j) already linked to %s (i), merging [count %d %d][fnicj %.2f %.2f]", master, iChunk, masterLink.getCount(), l.getCount(), masterLink.getFNICJ(), l.getFNICJ()));
                masterLink.setFNICJ(masterLink.getFNICJ() + l.getFNICJ());
            } else {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s (j) not already linked to %s (i), linking. %s is linked to %s", master, iChunk, master, masterSSC.getIAssociations(null)));
                Link4 newLink = (Link4) _linkageSystem.createLink(iChunk, master);
                newLink.setCount(((Link4) iLink).getCount());
                newLink.setFNICJ(((Link4) iLink).getFNICJ());
                masterSSC.addLink(newLink);
                ((ISubsymbolicChunk4) iChunk.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class)).addLink(newLink);
            }
            ((Link4) iLink).setCount(1);
            selfSSC.removeLink(iLink);
            ((ISubsymbolicChunk4) iChunk.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class)).removeLink(iLink);
        }
        links.clear();
        selfSSC.getJAssociations(links);
        for (IAssociativeLink jLink : links) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Testing old jLink %s", jLink));
            IChunk jChunk = jLink.getJChunk();
            if (jChunk.equals(self)) continue;
            Link4 masterLink = (Link4) masterSSC.getJAssociation(jChunk);
            if (masterLink != null) {
                Link4 l = (Link4) jLink;
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s (i) already linked to %s (j), merging [count %d %d][fnicj %.2f %.2f]", master, jChunk, masterLink.getCount(), l.getCount(), masterLink.getFNICJ(), l.getFNICJ()));
                masterLink.setFNICJ(masterLink.getFNICJ() + ((Link4) jLink).getFNICJ());
            } else {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s (j) not already linked to %s (i), linking. %s is linked to %s", jChunk, master, master, masterSSC.getJAssociations(null)));
                Link4 newLink = (Link4) _linkageSystem.createLink(master, jChunk);
                newLink.setCount(((Link4) jLink).getCount());
                newLink.setFNICJ(((Link4) jLink).getFNICJ());
                masterSSC.addLink(newLink);
                ((ISubsymbolicChunk4) jChunk.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class)).addLink(newLink);
            }
            ((Link4) jLink).setCount(1);
            selfSSC.removeLink(jLink);
            ((ISubsymbolicChunk4) jChunk.getSubsymbolicChunk().getAdapter(ISubsymbolicChunk4.class)).removeLink(jLink);
        }
        FastList.recycle(links);
    }

    @Override
    public void slotChanged(ChunkEvent ce) {
        IChunk iChunk = ce.getSource();
        Object oldValue = ce.getOldSlotValue();
        Object newValue = ce.getNewSlotValue();
        if (LOGGER.isDebugEnabled()) LOGGER.debug(iChunk + "." + ce.getSlotName() + "=" + newValue + " (was " + oldValue + ")");
        if (oldValue instanceof IChunk) _linkageSystem.linkSlotValue(iChunk, (IChunk) oldValue, true);
        if (newValue instanceof IChunk) _linkageSystem.linkSlotValue(iChunk, (IChunk) newValue, false);
    }
}

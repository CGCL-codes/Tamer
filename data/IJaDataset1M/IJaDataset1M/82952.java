package org.jactr.modules.pm.spatial.configural.encoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.chunk.ISymbolicChunk;
import org.jactr.core.chunktype.IChunkType;
import org.jactr.core.logging.Logger;
import org.jactr.core.model.IModel;
import org.jactr.core.slot.IMutableSlot;
import org.jactr.core.slot.ISlot;
import org.jactr.modules.pm.spatial.configural.IConfiguralModule;
import org.jactr.modules.pm.spatial.configural.info.ConfiguralInformation;
import org.jactr.modules.pm.spatial.util.VectorMath;

public class DefaultConfiguralRepresentationEncoder implements IConfiguralRepresentationEncoder {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(DefaultConfiguralRepresentationEncoder.class);

    public IChunk createChunk(IModel model, IChunkType configuralChunkType, String name) {
        if (name == null) name = "configural-chunk";
        IChunk newChunk = null;
        try {
            newChunk = model.getDeclarativeModule().createChunk(configuralChunkType, name).get();
            return newChunk;
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) LOGGER.error("Could not create configural chunk", e);
            return null;
        }
    }

    public void encode(ConfiguralInformation spatialInformation, IChunk chunk) {
        IChunk oldChunk = spatialInformation.getChunk();
        ISymbolicChunk sc = chunk.getSymbolicChunk();
        double[] centerAng = VectorMath.toAngular(spatialInformation.getCenter());
        double[] bottomAng = VectorMath.toAngular(spatialInformation.getBottomEdge());
        double[] topAng = VectorMath.toAngular(spatialInformation.getTopEdge());
        double[] leftAng = VectorMath.toAngular(spatialInformation.getLeftEdge());
        double[] rightAng = VectorMath.toAngular(spatialInformation.getRightEdge());
        double[] baseAng = VectorMath.toAngular(spatialInformation.getBase());
        ((IMutableSlot) sc.getSlot(IConfiguralModule.CENTER_BEARING_SLOT)).setValue(centerAng[0]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.CENTER_PITCH_SLOT)).setValue(centerAng[1]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.CENTER_RANGE_SLOT)).setValue(centerAng[2]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.BASE_RANGE_SLOT)).setValue(baseAng[2]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.TOP_PITCH_SLOT)).setValue(topAng[1]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.TOP_RANGE_SLOT)).setValue(topAng[2]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.BOTTOM_PITCH_SLOT)).setValue(bottomAng[1]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.BOTTOM_RANGE_SLOT)).setValue(bottomAng[2]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.RIGHT_BEARING_SLOT)).setValue(rightAng[0]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.RIGHT_RANGE_SLOT)).setValue(rightAng[2]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.LEFT_BEARING_SLOT)).setValue(leftAng[0]);
        ((IMutableSlot) sc.getSlot(IConfiguralModule.LEFT_RANGE_SLOT)).setValue(leftAng[2]);
        if (oldChunk != null) ((IMutableSlot) sc.getSlot(IConfiguralModule.IDENTIFIER_SLOT)).setValue(oldChunk.getSymbolicChunk().getSlot(IConfiguralModule.IDENTIFIER_SLOT).getValue());
        chunk.setMetaData(IConfiguralRepresentationEncoder.SPATIAL_INFORMATION_META_TAG, spatialInformation);
        IModel model = chunk.getModel();
        if (LOGGER.isDebugEnabled() || Logger.hasLoggers(model)) {
            StringBuilder sb = new StringBuilder("Precoded ");
            sb.append(chunk);
            String msg = sb.toString();
            LOGGER.debug(msg);
            Logger.log(model, IConfiguralModule.CONFIGURAL_LOG, msg);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Set slots values ");
            for (ISlot slot : sc.getSlots()) LOGGER.debug(sc.getName() + "." + slot);
        }
    }
}

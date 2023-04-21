package org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IAllowedString;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategoriesIds;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategory;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataModel;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMappingMember;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ISmallSuitability;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ISmallVegtationSuitabilityInputList;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ISmallWaterStorageSuitabilityInputList;
import org.kalypso.nofdpidss.core.common.ISuitabilities.WS_SEASON;
import org.kalypso.nofdpidss.core.i18n.Messages;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class SmallSuitabilityHandler extends AbstractProjectModelFeatureWrapper implements ISmallSuitability, Feature {

    public SmallSuitabilityHandler(final IProjectModel model, final Feature feature) {
        super(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.ISmallSuitability#getVegetationSuitabilityInputList()
   */
    public ISmallVegtationSuitabilityInputList getVegetationSuitabilityInputList() {
        final Feature feature = (Feature) getProperty(QN_VS_INPUT_LIST);
        return new SmallVegtationSuitabilityInputListHandler(getModel(), feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.ISmallSuitability#getVegetationSuitabilityResult()
   */
    public IAllowedString getVegetationSuitabilityResult(final IGeodataModel model) throws CoreException {
        final String result = (String) getProperty(QN_VS_RESULT);
        final IGeodataCategory[] categories = model.getCategories().getCategories(new String[] { IGeodataCategoriesIds.ID_VEGETATION_SUITABILITY_SMALL });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.SmallSuitabilityHandler_0);
        final IGeodataCategory category = categories[0];
        final IMappingMember[] mappingMembers = category.getDataStructureMember().getMappingMember().getMappingMembers();
        if (mappingMembers.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.SmallSuitabilityHandler_1);
        final IMappingMember member = mappingMembers[0];
        final IAllowedString[] strings = member.getAllowedMembers().getStrings();
        for (final IAllowedString allowedString : strings) {
            if (allowedString.getName().equals(result)) return allowedString;
        }
        return null;
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.ISmallSuitability#getWaterStorageSuitabilityInputList(org.kalypso.nofdpidss.core.common.ISuitabilities.WS_SEASON)
   */
    public ISmallWaterStorageSuitabilityInputList getWaterStorageSuitabilityInputList(final WS_SEASON season) {
        if (WS_SEASON.eSummer.equals(season)) {
            final Feature feature = (Feature) getProperty(QN_MEASURE_MEASURE_WS_SUMMER_INPUT_LIST);
            return new SmallWaterStorageSuitabilityInputListHandler(getModel(), feature);
        } else if (WS_SEASON.eWinter.equals(season)) {
            final Feature feature = (Feature) getProperty(QN_MEASURE_MEASURE_WS_WINTER_INPUT_LIST);
            return new SmallWaterStorageSuitabilityInputListHandler(getModel(), feature);
        } else throw new NotImplementedException();
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.ISmallSuitability#getWaterStorageSuitabilityResult(org.kalypso.nofdpidss.core.common.ISuitabilities.WS_SEASON)
   */
    public IAllowedString getWaterStorageSuitabilityResult(final IGeodataModel model, final WS_SEASON season) throws CoreException {
        String result;
        if (WS_SEASON.eSummer.equals(season)) result = (String) getProperty(QN_WS_RESULT_SUMMER); else if (WS_SEASON.eWinter.equals(season)) result = (String) getProperty(QN_WS_RESULT_WINTER); else throw new NotImplementedException();
        final IGeodataCategory[] categories = model.getCategories().getCategories(new String[] { IGeodataCategoriesIds.ID_WATER_STORAGE_SUITABILITY });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.SmallSuitabilityHandler_2);
        final IGeodataCategory category = categories[0];
        final IMappingMember[] mappingMembers = category.getDataStructureMember().getMappingMember().getMappingMembers();
        if (mappingMembers.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.SmallSuitabilityHandler_3);
        final IMappingMember member = mappingMembers[0];
        final IAllowedString[] strings = member.getAllowedMembers().getStrings();
        for (final IAllowedString allowedString : strings) {
            if (allowedString.getName().equals(result)) return allowedString;
        }
        return null;
    }
}

package org.docflower.server.home;

import java.util.*;
import javax.xml.namespace.QName;
import org.docflower.objectserializer.params.*;
import org.docflower.serializationutils.IMetanodeSerializable;
import org.docflower.serializer.metanodes.*;
import org.docflower.server.filters.*;

public abstract class GenericPagedListMetanodeSerializable implements IMetanodeSerializable {

    public abstract List<?> getList();

    /**
	 * Return the usecase QName to handle list of this metanode
	 */
    protected abstract QName getUsecaseQName();

    @SuppressWarnings("unchecked")
    @Override
    public void prepareMetanodeParameters(AbstractParamHolder parameterHolder) {
        ArrayList<IMetanodeParameter> paramList = new ArrayList<IMetanodeParameter>();
        Map<Object, List<IMetanodeParameter>> params = null;
        Object tmpParams = parameterHolder.getParamByName(ParamsConsts.PARAM_METANODES_SERIALIZATION_PARAMETERS);
        if (tmpParams != null) {
            params = (Map<Object, List<IMetanodeParameter>>) tmpParams;
        } else {
            params = new IdentityHashMap<Object, List<IMetanodeParameter>>();
        }
        BaseFilter filter = getFilter();
        if (filter != null) {
            paramList.add(filter);
        }
        BaseStaticFilter staticFilter = getStaticFilter();
        if (staticFilter != null) {
            paramList.add(staticFilter);
        }
        PagedNodeParameters pagedNodeParams = new PagedNodeParameters();
        pagedNodeParams.setUsecase(getUsecaseQName());
        pagedNodeParams.setMode(getMode());
        pagedNodeParams.setCount(0);
        paramList.add(pagedNodeParams);
        params.put(getList(), paramList);
        parameterHolder.replaceParam(ParamsConsts.PARAM_METANODES_SERIALIZATION_PARAMETERS, params);
    }

    /**
	 * Override this method to add the usecase mode if any.
	 */
    protected String getMode() {
        return null;
    }

    /**
	 * Override this method to add list static filter if any.
	 */
    protected BaseStaticFilter getStaticFilter() {
        return null;
    }

    /**
	 * Override this method to add list filter if any.
	 */
    protected BaseFilter getFilter() {
        return null;
    }
}

package net.sf.refactorit.classmodel;

import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.common.util.CollectionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Safonov
 */
public class BinConvertorTypeRef extends BinTreeTypeRef {

    private BinTypeRef[] methodTypeArgs;

    private BinTypeRef[] methodArgs;

    private BinTypeRef[] methodParams;

    private BinTypeRef convertor;

    /** Ensures that this new ref looks the same way as the old one */
    public BinConvertorTypeRef(final BinTypeRef typeRef, final BinTypeRef convertor) {
        super(typeRef.getCompilationUnit(), typeRef.getNode(), typeRef);
        setTypeArguments(typeRef.getTypeArguments());
        if (getTypeRefAsIs().isSpecific()) {
            addChild(((BinSpecificTypeRef) getTypeRefAsIs()).getChild());
        }
        this.convertor = convertor;
    }

    public void setMethodParams(BinTypeRef[] methodParams, BinTypeRef[] methodArgs) {
        this.methodArgs = methodArgs;
        this.methodParams = methodParams;
    }

    public void setMethodTypeArgs(BinTypeRef[] methodTypeArgs) {
        this.methodTypeArgs = methodTypeArgs;
    }

    public BinTypeRef findTypeArgumentByParameter(BinTypeRef typeParameter) {
        BinTypeRef arg = findArgument(typeParameter, this.getTypeArguments());
        if (arg != null && arg != typeParameter) {
            return arg;
        }
        if (this.methodTypeArgs != null) {
            arg = findArgument(typeParameter, this.methodTypeArgs);
            if (arg != null && arg != typeParameter) {
                return arg;
            }
        }
        if (this.methodParams != null) {
            arg = findTypeArgumentInMethodParams(typeParameter, this.methodParams, this.methodArgs);
            if (arg != null && arg != typeParameter) {
                return arg;
            }
        }
        if (arg == null || arg == typeParameter) {
            arg = super.findTypeArgumentByParameter(typeParameter);
            if (arg != null && arg != typeParameter) {
                return arg;
            }
        }
        if (this.convertor != null) {
            arg = findArgument(typeParameter, convertor.getTypeArguments());
            if (arg != null && arg != typeParameter) {
                return arg;
            }
        }
        return typeParameter;
    }

    public static BinTypeRef findTypeArgumentInMethodParams(final BinTypeRef typeParameter, final BinTypeRef[] methodParams, final BinTypeRef[] methodArgs) {
        Map conversionMap = CollectionUtil.toMap(methodParams, methodArgs);
        while (!conversionMap.isEmpty()) {
            BinTypeRef arg = (BinTypeRef) conversionMap.get(typeParameter);
            if (arg != null) {
                return arg;
            }
            HashMap newMap = new HashMap(conversionMap.size());
            Iterator pars = conversionMap.keySet().iterator();
            while (pars.hasNext()) {
                BinTypeRef par = (BinTypeRef) pars.next();
                BinTypeRef argg = (BinTypeRef) conversionMap.get(par);
                if (argg != null) {
                    CollectionUtil.toMap(newMap, par.getTypeArguments(), argg.getTypeArguments());
                } else if (Assert.enabled) {
                }
            }
            conversionMap = newMap;
        }
        return typeParameter;
    }

    public static BinTypeRef findArgument(final BinTypeRef typeParameter, final BinTypeRef[] args) {
        if (args == null || args.length == 0) {
            return typeParameter;
        }
        List potentialArguments = Arrays.asList(args);
        List nextList = null;
        while (!potentialArguments.isEmpty()) {
            nextList = new ArrayList(3);
            for (int i = 0, max = potentialArguments.size(); i < max; i++) {
                BinTypeRef arg = (BinTypeRef) potentialArguments.get(i);
                if (arg.isSpecific()) {
                    if (typeParameter.equals(((BinSpecificTypeRef) arg).getCorrespondingTypeParameter())) {
                        return arg;
                    }
                    BinTypeRef[] nextArgs = arg.getTypeArguments();
                    if (nextArgs != null && nextArgs.length > 0) {
                        CollectionUtil.addAll(nextList, nextArgs);
                    }
                }
            }
            potentialArguments = nextList;
        }
        return typeParameter;
    }
}

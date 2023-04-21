package net.simplemodel.core.generator.internal;

import net.simplemodel.core.generator.*;

public class MultiValueOppositeSynchronizedNotifyingListImplTemplate implements ITemplate {

    protected static String nl;

    public static synchronized MultiValueOppositeSynchronizedNotifyingListImplTemplate create(String lineSeparator) {
        nl = lineSeparator;
        MultiValueOppositeSynchronizedNotifyingListImplTemplate result = new MultiValueOppositeSynchronizedNotifyingListImplTemplate();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "class MultiValueOppositeSynchronizedNotifyingListImpl<V, E> extends SynchronizedNotifyingListImpl<E> {" + NL + "\t";

    protected final String TEXT_2 = "private final MultiValueAttribute<E,V> inverseAttribute;" + NL + "\t";

    protected final String TEXT_3 = "private final MultiValueAttribute<V,E> attribute;" + NL + "\t";

    protected final String TEXT_4 = "private final V owner;" + NL + "\t";

    protected final String TEXT_5 = "public MultiValueOppositeSynchronizedNotifyingListImpl(V owner, MultiValueAttribute<V,E> attribute, MultiValueAttribute<E,V> inverseAttribute, ";

    protected final String TEXT_6 = " readLock,";

    protected final String TEXT_7 = " writeLock) {" + NL + "\t\tsuper(readLock,writeLock);" + NL + "\t\tif (owner == null)" + NL + "\t\t\tthrow new IllegalArgumentException();" + NL + "\t\tif (attribute == null)" + NL + "\t\t\tthrow new IllegalArgumentException();" + NL + "\t\tif (inverseAttribute == null)" + NL + "\t\t\tthrow new IllegalArgumentException();" + NL + "\t\tthis.attribute = attribute;" + NL + "\t\tthis.inverseAttribute = inverseAttribute;" + NL + "\t\tthis.owner = owner;" + NL + "\t}" + NL + "\tprivate ChangingAttributeContext context;" + NL + "\t";

    protected final String TEXT_8 = "protected boolean preAdd(int index, E value){" + NL + "\t\tChangingAttributeContext context = createChangingAttributeContextAddToList(value, attribute);" + NL + "\t\tif (context==null)" + NL + "\t\t\treturn false;" + NL + "\t\tthis.context=context;" + NL + "\t\treturn true;" + NL + "\t}" + NL + "\t";

    protected final String TEXT_9 = "protected void onAdd(int index, E value){" + NL + "\t\tif (value!=null)" + NL + "\t\t\tcontext.expectAddToList(value).addToList(inverseAttribute, value, owner);" + NL + "\t}" + NL + "\t";

    protected final String TEXT_10 = "protected void finallyAdd(int index, E value){" + NL + "\t\tcontext.done();" + NL + "\t\tcontext=null;" + NL + "\t}" + NL + "\t";

    protected final String TEXT_11 = "protected boolean preRemove(int index, E value){" + NL + "\t\tChangingAttributeContext context = createChangingAttributeContextRemoveFromList(value, attribute);" + NL + "\t\tif (context==null)" + NL + "\t\t\treturn false;" + NL + "\t\tthis.context=context;" + NL + "\t\treturn true;" + NL + "\t}" + NL + "\t";

    protected final String TEXT_12 = "protected void onRemove(int index, E value){" + NL + "\t\tif (value!=null)" + NL + "\t\t\tcontext.expectRemoveFromList(value).removeFromList(inverseAttribute, value, owner);" + NL + "\t}" + NL + "\t";

    protected final String TEXT_13 = "protected void finallyRemove(int index, E value){" + NL + "\t\tcontext.done();" + NL + "\t\tcontext=null;" + NL + "\t}" + NL + "\t";

    protected final String TEXT_14 = "protected boolean preSet(int index, E oldValue, E newValue){" + NL + "\t\tif (oldValue == newValue)" + NL + "\t\t\treturn false;" + NL + "\t\tChangingAttributeContext context = createChangingAttributeContextSetToList(newValue, attribute);" + NL + "\t\tif (context==null)" + NL + "\t\t\treturn false;" + NL + "\t\tthis.context=context;" + NL + "\t\treturn true;" + NL + "\t}" + NL + "\t";

    protected final String TEXT_15 = "protected void onSet(int index, E oldValue, E newValue){" + NL + "\t\tif (oldValue!=null)" + NL + "\t\t\tcontext.expectRemoveFromList(oldValue).removeFromList(inverseAttribute, oldValue, owner);" + NL + "\t\tif (newValue!=null)" + NL + "\t\t\tcontext.expectAddToList(newValue).addToList(inverseAttribute, newValue, owner);" + NL + "\t}" + NL + "\t";

    protected final String TEXT_16 = "protected void finallySet(int index, E oldValue, E newValue){" + NL + "\t\tcontext.done();" + NL + "\t\tcontext=null;" + NL + "\t}" + NL + "}";

    void willNotGenerate() {
        throw new WillNotGenerateException();
    }

    @Override
    public String generate(ITemplateContext argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        IImportBlock importBlock = argument.getImportBlock();
        stringBuffer.append(TEXT_1);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_2);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_3);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_4);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_5);
        stringBuffer.append(importBlock.imprt(CommonTypes.LOCK));
        stringBuffer.append(TEXT_6);
        stringBuffer.append(importBlock.imprt(CommonTypes.LOCK));
        stringBuffer.append(TEXT_7);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_8);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_9);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_10);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_11);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_12);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_13);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_14);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_15);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_16);
        return stringBuffer.toString();
    }
}

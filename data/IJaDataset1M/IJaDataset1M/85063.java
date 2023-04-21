package org.jgentleframework.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jgentleframework.core.handling.DefinitionManager;
import org.jgentleframework.reflection.annohandler.AnnotationBeanProcessor;
import org.jgentleframework.reflection.annohandler.AnnotationHandler;
import org.jgentleframework.reflection.annohandler.AnnotationPostProcessor;
import org.jgentleframework.reflection.annohandler.PointStatus;
import org.jgentleframework.reflection.metadata.AnnotationMetadata;
import org.jgentleframework.utils.Assertor;
import org.jgentleframework.utils.ReflectUtils;

/**
 * This class is responsible for annotation interpreter.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Sep 4, 2007
 */
public abstract class AbstractVisitorHandler implements IAnnotationVisitor {

    /** The handler list. */
    @SuppressWarnings("unchecked")
    Map<Class, AnnotationBeanProcessor> handlerList;

    /** The successors. */
    protected List<DefinitionPostProcessor> successors;

    /** The definition manager. */
    public DefinitionManager definitionManager = null;

    /**
	 * Instantiates a new abstract visitor handler.
	 * 
	 * @param defManager
	 *            the def manager
	 */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> AbstractVisitorHandler(DefinitionManager defManager) {
        this.definitionManager = defManager;
        this.handlerList = new HashMap<Class, AnnotationBeanProcessor>();
        this.successors = new ArrayList<DefinitionPostProcessor>();
    }

    /**
	 * Builds the.
	 * 
	 * @param element
	 *            the element
	 * @param containter
	 *            the containter
	 * @param definitionManager
	 *            the definition manager
	 * @return the anno meta
	 * @throws Exception
	 *             the exception
	 */
    protected abstract AnnotationMetadata build(Annotation element, AnnotationMetadata containter, DefinitionManager definitionManager) throws Exception;

    /**
	 * Interprets annotation.
	 * 
	 * @param annoArray
	 *            the anno array
	 * @param rootAnnoMeta
	 *            the root anno meta
	 */
    @SuppressWarnings("unchecked")
    private void visitAnnotation(Annotation[] annoArray, AnnotationMetadata rootAnnoMeta) {
        for (Annotation element : annoArray) {
            AnnotationBeanProcessor<?> annoHandler = this.handlerList.get(element.annotationType());
            AnnotationPostProcessor app = null;
            AnnotationMetadata result = null;
            try {
                if (annoHandler != null) {
                    PointStatus ps = (PointStatus) (ReflectUtils.isCast(PointStatus.class, annoHandler) ? annoHandler : null);
                    if (ReflectUtils.isCast(AnnotationPostProcessor.class, annoHandler)) {
                        if ((ps != null && ps.isEnable()) || ps == null) {
                            app = (AnnotationPostProcessor) annoHandler;
                            app.before(element, rootAnnoMeta, annoArray, rootAnnoMeta.getKey());
                        }
                    }
                    if (ReflectUtils.isCast(AnnotationHandler.class, annoHandler)) {
                        if ((ps != null && ps.isEnable()) || ps == null) {
                            AnnotationHandler ahl = (AnnotationHandler) annoHandler;
                            result = ahl.handleVisit(element, rootAnnoMeta, this.definitionManager);
                        }
                    } else {
                        result = build(element, rootAnnoMeta, this.definitionManager);
                    }
                    if (ReflectUtils.isCast(AnnotationPostProcessor.class, annoHandler)) {
                        if ((ps != null && ps.isEnable()) || ps == null) {
                            app = (AnnotationPostProcessor) annoHandler;
                            app.after(element, rootAnnoMeta, result, annoArray, rootAnnoMeta.getKey());
                        }
                    }
                } else {
                    result = build(element, rootAnnoMeta, this.definitionManager);
                }
            } catch (Exception e) {
                if (app != null) {
                    try {
                        app.catchException(e, element, rootAnnoMeta, result, annoArray, rootAnnoMeta.getKey());
                    } catch (AnnotationBeanException e1) {
                        continue;
                    } catch (RuntimeException e1) {
                        throw e1;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (ReflectUtils.isCast(RuntimeException.class, e)) {
                    throw (RuntimeException) e;
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    public void visit(Annotation[] annoArray, AnnotationMetadata rootAnnoMeta) {
        if (!this.successors.isEmpty()) {
            for (DefinitionPostProcessor obj : successors) {
                PointStatus ps = (PointStatus) (ReflectUtils.isCast(PointStatus.class, obj) ? obj : null);
                if ((ps != null && ps.isEnable()) || ps == null) {
                    try {
                        obj.beforePost(annoArray, rootAnnoMeta);
                    } catch (Exception e) {
                        boolean result = obj.catchException(e, annoArray, rootAnnoMeta, true);
                        if (result == false) {
                            return;
                        }
                    }
                }
            }
        } else {
            try {
                ReflectUtils.invokeMethod(this, "beforePost", new Class[] { annoArray.getClass(), rootAnnoMeta.getClass() }, new Object[] { annoArray, rootAnnoMeta }, true, true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        visitAnnotation(annoArray, rootAnnoMeta);
        if (!this.successors.isEmpty()) {
            for (DefinitionPostProcessor obj : successors) {
                PointStatus ps = (PointStatus) (ReflectUtils.isCast(PointStatus.class, obj) ? obj : null);
                if ((ps != null && ps.isEnable()) || ps == null) {
                    try {
                        obj.afterPost(annoArray, rootAnnoMeta);
                    } catch (Exception e) {
                        boolean result = obj.catchException(e, annoArray, rootAnnoMeta, false);
                        if (result == false) {
                            return;
                        }
                    }
                }
            }
        } else {
            try {
                ReflectUtils.invokeMethod(this, "afterPost", new Class[] { annoArray.getClass(), rootAnnoMeta.getClass() }, new Object[] { annoArray, rootAnnoMeta }, true, true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T extends Annotation> AnnotationBeanProcessor<?> addAnnotationBeanProcessor(Class<T> key, AnnotationBeanProcessor<T> handler) {
        Assertor.notNull(key);
        Assertor.notNull(handler);
        return this.handlerList.put(key, handler);
    }

    @Override
    public <T extends Annotation> AnnotationBeanProcessor<?> removeAnnotationBeanProcessor(Class<T> key) {
        return this.handlerList.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> void replaceAnnotationBeanProcessor(AnnotationBeanProcessor<T> oldHandler, AnnotationBeanProcessor<T> newHandler) {
        Assertor.notNull(oldHandler);
        Assertor.notNull(newHandler);
        for (Entry<Class, AnnotationBeanProcessor> entry : this.handlerList.entrySet()) {
            if (entry.getValue().equals(oldHandler)) {
                this.addAnnotationBeanProcessor(entry.getKey(), newHandler);
            }
        }
    }

    /**
	 * Returns the number of {@link DefinitionPostProcessor} in this visitor.
	 * 
	 * @return int
	 */
    public int countDPP() {
        return this.successors.size();
    }

    /**
	 * Returns the {@link DefinitionPostProcessor} according to its given index
	 * location.
	 * 
	 * @param index
	 *            the given index.
	 * @return the definition post processor
	 */
    public DefinitionPostProcessor getDefinitionPostProcessor(int index) {
        Assertor.hasValidIndex(this.successors, index);
        return this.successors.get(index);
    }

    /**
	 * Adds the {@link DefinitionPostProcessor} to this visitor.
	 * 
	 * @param dpp
	 *            the given {@link DefinitionPostProcessor}
	 */
    public void addDefinitionPostProcessor(DefinitionPostProcessor dpp) {
        Assertor.notNull(dpp);
        if (this.successors == null) {
            this.successors = new ArrayList<DefinitionPostProcessor>();
        }
        if (!this.successors.contains(dpp)) {
            this.successors.add(dpp);
        }
    }

    /**
	 * Adds the {@link DefinitionPostProcessor} to this visitor.
	 * 
	 * @param dpp
	 *            the implementtation class of {@link DefinitionPostProcessor}
	 */
    public void addDefinitionPostProcessor(Class<? extends DefinitionPostProcessor> dpp) {
        Assertor.notNull(dpp);
        addDefinitionPostProcessor(ReflectUtils.createInstance(dpp));
    }

    /**
	 * Removes specified {@link DefinitionPostProcessor} according to given
	 * index location.
	 * 
	 * @param index
	 *            the index
	 * @return returns removed corresponding {@link DefinitionPostProcessor}
	 */
    public DefinitionPostProcessor removeDefinitionPostProcessor(int index) {
        Assertor.hasValidIndex(this.successors, index);
        return this.successors.remove(index);
    }

    /**
	 * Removes specified {@link DefinitionPostProcessor}.
	 * 
	 * @param dpp
	 *            the specified {@link DefinitionPostProcessor} need to be
	 *            removed.
	 * @return <b>true</b> if this visitor contained the specified
	 *         {@link DefinitionPostProcessor}.
	 */
    public boolean removeDefinitionPostProcessor(DefinitionPostProcessor dpp) {
        Assertor.notNull(dpp);
        return this.successors.remove(dpp);
    }
}

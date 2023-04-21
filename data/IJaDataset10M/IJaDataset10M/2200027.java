package org.magicdroid.features;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.magicdroid.commons.Injector.Inject;
import org.magicdroid.commons.MagicObject;
import org.magicdroid.commons.MagicObject.Interface;
import org.magicdroid.commons.MagicObject.Invocation;
import org.magicdroid.commons.MagicObject.This;
import org.magicdroid.commons.MethodLookupMap;
import org.magicdroid.commons.Refactor;
import org.magicdroid.commons.Structures.Lazy;
import org.magicdroid.services.UnitOfWork;

public interface WorkingCopyFeature {

    WorkingCopy loadFromMap(Map<String, Object> map);

    WorkingCopy clear();

    Class<? extends EntityFeature> internalGetEntityType();

    void internalSetEntityType(Class<? extends EntityFeature> entityType);

    void consume();

    boolean isConsumed();

    void internalSetUnitOfWork(UnitOfWork uow);

    class WorkingCopyConcern implements MagicObject.Concern {

        private UnitOfWork unitOfWork;

        private EntityFeature ref;

        @This
        EntityFeature entity;

        @Inject
        public WorkingCopyConcern() {
        }

        @Override
        public Object invoke(Invocation context) throws Throwable {
            if (context.getMethod().getName().equals("isConsumed")) return context.proceed();
            if (context.getMethod().getName().equals("consume")) {
                if (this.unitOfWork == null) throw new IllegalStateException("You forgot to call workingCopy.internalSetUnitOfWork!");
                this.ref = this.unitOfWork.lookup(this.entity.getId());
                return null;
            }
            if (context.getMethod().getName().equals("internalSetUnitOfWork")) {
                this.unitOfWork = (UnitOfWork) context.getParams()[0];
                return null;
            }
            if (this.ref != null) return context.getMethod().invoke(this.ref, context.getParams());
            return context.proceed();
        }
    }

    class MixinImpl<W extends WorkingCopy> implements WorkingCopyFeature {

        @This
        private WorkingCopy workingCopy;

        @Interface
        private Class<? extends WorkingCopy> type;

        private Lazy<Map<String, Method>> methods = new Lazy<Map<String, Method>>() {

            @Override
            protected Map<String, Method> load() {
                return new MethodLookupMap(type);
            }
        };

        private Class<? extends EntityFeature> entityType;

        public MixinImpl() {
        }

        @Override
        public WorkingCopy loadFromMap(Map<String, Object> map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) this.set(entry.getKey(), entry.getValue());
            return this.workingCopy;
        }

        @Override
        public WorkingCopy clear() {
            for (String property : this.workingCopy.metaAsMap().keySet()) this.set(property, null);
            return this.workingCopy;
        }

        private void set(String property, Object value) {
            try {
                if (property.equals(IdentifyFeature.Keys.ID)) {
                    this.workingCopy.internalSet(IdentifyFeature.Keys.ID, value);
                    return;
                }
                String setter = Refactor.setter(property);
                Method method = this.methods.get().get(setter);
                if (method == null) throw new NoSuchMethodException(type + "." + setter);
                method.invoke(this.workingCopy, value);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            }
        }

        @Override
        public void internalSetEntityType(Class<? extends EntityFeature> entityType) {
            this.entityType = entityType;
        }

        public Class<? extends EntityFeature> internalGetEntityType() {
            return entityType;
        }

        @Override
        public boolean isConsumed() {
            return !WorkingCopy.class.isAssignableFrom(this.workingCopy.metaEntityType());
        }

        @Override
        public void consume() {
            throw new UnsupportedOperationException("You forgot " + WorkingCopyConcern.class + " in your working-copy class configuration!");
        }

        @Override
        public void internalSetUnitOfWork(UnitOfWork uow) {
            throw new UnsupportedOperationException("You forgot " + WorkingCopyConcern.class + " in your working-copy class configuration!");
        }
    }
}

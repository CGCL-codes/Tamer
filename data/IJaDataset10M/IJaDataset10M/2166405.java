package org.t2framework.lucy;

import org.t2framework.commons.exception.NoSuchComponentException;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.PackageDescHolder;
import org.t2framework.lucy.exception.CyclicReferenceRuntimeException;
import org.t2framework.lucy.exception.TooManyRegistrationException;

/**
 * <#if locale="en">
 * <p>
 * A simple IoC container without difficult things.
 * </p>
 * <#else>
 * <p>
 * LucyはシンプルなIoCコンテナです.
 * </p>
 * </#if>
 * 
 * @author shot
 */
public interface Lucy extends PackageDescHolder, ComponentLoader {

    /**
	 * <#if locale="en">
	 * <p>
	 * Initialize {@link Lucy}.
	 * </p>
	 * <#else>
	 * <p>
	 * Lucyを初期化します.
	 * </p>
	 * </#if>
	 */
    void init();

    /**
	 * <#if locale="en">
	 * <p>
	 * Destroy this Lucy instance.
	 * </p>
	 * <#else>
	 * <p>
	 * Lucyを破棄します.Lucyが保持する開放されるべきリソースも同時に開放されます.
	 * </p>
	 * </#if>
	 */
    void destroy();

    /**
	 * <#if locale="en">
	 * <p>
	 * Register the class. The class must not be null.
	 * </p>
	 * <#else>
	 * <p>
	 * Lucyに渡されたClassを登録します.ClassはNullであってはいけません.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param componentClass
	 *            (must not be null)
	 * @return {@link Lucy}
	 */
    <T> Lucy register(Class<? extends T> componentClass);

    /**
	 * <#if locale="en">
	 * <p>
	 * Register the instance.The instance must not be null.
	 * </p>
	 * <#else>
	 * <p>
	 * Lucyに渡されたインスタンスを登録します.インスタンスはnullであってはいけません.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param t
	 * @return {@link Lucy}
	 */
    <T> Lucy register(T t);

    /**
	 * <#if locale="en">
	 * <p>
	 * Register class with an identifier.
	 * </p>
	 * <#else>
	 * <p>
	 * Classを識別子と共に登録します.Class、識別子共にnullであってはいけません.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param componentClass
	 * @param identifier
	 * @return
	 */
    <T> Lucy register(Class<? extends T> componentClass, Object identifier);

    /**
	 * <#if locale="en">
	 * <p>
	 * Register an instance with an identifier.Both instance and identifier must
	 * not be null.
	 * </p>
	 * <#else>
	 * <p>
	 * インスタンスを識別子と共に登録します.インスタンス、識別子共にnullであってはいけません.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param t
	 *            (must not be null)
	 * @param identifier
	 *            (must not be null)
	 * @return {@link Lucy}
	 */
    <T> Lucy register(T t, Object identifier);

    /**
	 * <#if locale="en">
	 * <p>
	 * Register {@link BeanDesc}, a descriptor class of a bean.
	 * </p>
	 * <#else>
	 * <p>
	 * Beanの記述情報クラスである、BeanDescを登録します.BeanDescはnullであってはいけません.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param beanDesc
	 *            (must not be null)
	 * @return {@link Lucy}
	 */
    <T> Lucy register(BeanDesc<? extends T> beanDesc);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Register {@link BeanDesc} with an identifier.
	 * </p>
	 * <#else>
	 * <p>
	 * {@code BeanDesc}を識別子つきで登録します．
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param beanDesc
	 *            (must not be null)
	 * @param identifier
	 *            (must not be null)
	 * @return
	 */
    <T> Lucy register(BeanDesc<? extends T> beanDesc, Object identifier);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get component by class.If {@link Lucy} has more than one component, it
	 * will throw {@link TooManyRegistrationException} by default.
	 * </p>
	 * <#else>
	 * <p>
	 * {@link Class}をキーにコンポーネントを取得します.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 * @throws TooManyRegistrationException
	 * @throws CyclicReferenceRuntimeException
	 * @throws NoSuchComponentException
	 */
    <T> T get(Class<? super T> key) throws TooManyRegistrationException, CyclicReferenceRuntimeException, NoSuchComponentException;

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get component by identifier.
	 * </p>
	 * <#else>
	 * <p>
	 * {@link Object}をキーにコンポーネントを取得します.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param identifier
	 * @return
	 */
    <T> T get(Object identifier);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get all components by class.This method does not throw
	 * 
	 * @link TooManyListenersException
	 *       </p>
	 *       in design and will get multiple components. } <#else>
	 *       <p>
	 *       {@link Class} をキーに取得できるコンポーネントの配列を返します.キーで登録されているコンポーネントが
	 *       無い場合、空の配列を返します.
	 *       </p>
	 *       </#if>
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 * @throws CyclicReferenceRuntimeException
	 */
    <T> T[] getAll(Class<? super T> key) throws CyclicReferenceRuntimeException;

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get BeanDesc by identifier.
	 * </p>
	 * <#else>
	 * <p>
	 * オブジェクトの識別子をキーに{@link BeanDesc}を取得します.
	 * キーで登録されているコンポーネントが無い場合、デフォルトではnullを返します. コンポーネントが無い場合の挙動は
	 * {@link BeanDescStrategy}で変更することが出来ます.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param identifier
	 * @return
	 * @throws CyclicReferenceRuntimeException
	 */
    <T> BeanDesc<T> getBeanDesc(Object identifier) throws CyclicReferenceRuntimeException;

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get array of BeanDesc by class.This method does not throw
	 * 
	 * @link TooManyRegistrationException
	 *       </p>
	 *       in design and will get multiple components. } <#else>
	 *       <p>
	 *       {@link Class}をキーに{@link BeanDesc}の配列を返します.このメソッドは
	 *       {@link TooManyRegistrationException}は発生しません. {@link Class}
	 *       がキーで登録されているコンポーネントが無い場合、空の配列を返します.
	 *       </p>
	 *       </#if>
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 * @throws CyclicReferenceRuntimeException
	 */
    <T> BeanDesc<T>[] getBeanDescs(Class<? super T> key) throws CyclicReferenceRuntimeException;

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get {@link BeanDesc} by class.
	 * </p>
	 * <#else>
	 * <p>
	 * {@link Class}をキーにして{@link BeanDesc}を取得します.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 * @throws TooManyRegistrationException
	 * @throws CyclicReferenceRuntimeException
	 */
    <T> BeanDesc<T> getBeanDesc(Class<? super T> key) throws TooManyRegistrationException, CyclicReferenceRuntimeException;

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * True if a component exists by the class.
	 * </p>
	 * <#else>
	 * <p>
	 * Classにマッチするコンポーネントが存在するかチェックします.存在する場合、trueを返します.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 */
    <T> boolean hasComponent(Class<T> key);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * True if a component exists by identifier.
	 * </p>
	 * <#else>
	 * <p>
	 * 識別子にマッチするコンポーネントが存在するかチェックします.存在する場合、trueを返します.
	 * </p>
	 * </#if>
	 * 
	 * @param identifier
	 * @return
	 */
    boolean hasComponent(Object identifier);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Inject dependency.
	 * </p>
	 * <#else>
	 * <p>
	 * インスタンスTに対して、依存解決をします.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
    <T> T injectDependency(T t);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get lifecycle phase of Lucy.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return Lifecycle
	 */
    Lifecycle getLifecycle();

    /**
	 * <#if locale="en">
	 * <p>
	 * Pretend as if gets component for the original class but actually returns
	 * the new one instead.This method is expected to use at testing time.The
	 * main scenario is that there is a component loaded by xml configuration,
	 * but somehow you want replace mock one for testing purpose, that's the
	 * situation we expect. This method works only when lifecycle phase is
	 * {@link Lifecycle#CONTAINER_PREPARED} , which means the time before
	 * component creating and injecting.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param original
	 * @param newone
	 * @since 0.5.2
	 */
    <T> void pretend(Class<? extends T> original, Class<? extends T> newone);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Pretend and swap the component that is found by identifier to the new
	 * one.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param identifier
	 * @param newone
	 * @since 0.5.2
	 */
    <T> void pretend(Object identifier, Class<? extends T> newone);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Pretend and swap the component that is found by class to the new one.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param original
	 * @param instance
	 * @since 0.5.2
	 */
    <T> void pretend(Class<? extends T> original, T instance);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Pretend and swap the component that is found by identifier to the new
	 * one.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param identifier
	 * @param instance
	 * @since 0.5.2
	 */
    <T> void pretend(Object identifier, T instance);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Execute for preparation such as prepare eager-singletons.
	 * 
	 * Eager-singleton is the singleton instances which is set up faster than
	 * other instances such as normal singleton or prototype.Eager-singleton is
	 * needed because normal singleton is done at first {@link Lucy#get(Class)}
	 * is called and sometime it is too late to component initialization, user
	 * expect at least singleton instances should be ready for serve.
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @since 0.6.2
	 */
    void prepare();
}

package de.wadndadn.deliciousj.cache.impl;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import de.wadndadn.deliciousj.PostsApi;
import de.wadndadn.deliciousj.SuggestType;
import de.wadndadn.deliciousj.TagBundlesApi;
import de.wadndadn.deliciousj.TagsApi;
import de.wadndadn.deliciousj.Update;
import de.wadndadn.deliciousj.UpdateApi;
import de.wadndadn.deliciousj.cache.Bundle;
import de.wadndadn.deliciousj.cache.Cache;
import de.wadndadn.deliciousj.cache.DeliciousjCacheFactory;
import de.wadndadn.deliciousj.cache.DeliciousjCachePackage;
import de.wadndadn.deliciousj.cache.Post;
import de.wadndadn.deliciousj.cache.Suggest;
import de.wadndadn.deliciousj.cache.Tag;
import de.wadndadn.deliciousj.errorhandling.ConfigurationException;
import de.wadndadn.deliciousj.errorhandling.ThrottledException;
import de.wadndadn.deliciousj.errorhandling.UnauthorizedException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cache</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getPosts <em>Posts</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getTags <em>Tags</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getBundles <em>Bundles</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getBundlesApi <em>Bundles Api</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getPostsApi <em>Posts Api</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getTagsApi <em>Tags Api</em>}</li>
 *   <li>{@link de.wadndadn.deliciousj.cache.impl.CacheImpl#getUpdateApi <em>Update Api</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CacheImpl extends EObjectImpl implements Cache {

    /**
	 * The cached value of the '{@link #getPosts() <em>Posts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPosts()
	 * @generated
	 * @ordered
	 */
    protected EList<Post> posts;

    /**
	 * The cached value of the '{@link #getTags() <em>Tags</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTags()
	 * @generated
	 * @ordered
	 */
    protected EList<Tag> tags;

    /**
	 * The cached value of the '{@link #getBundles() <em>Bundles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundles()
	 * @generated
	 * @ordered
	 */
    protected EList<Bundle> bundles;

    /**
	 * The default value of the '{@link #getBundlesApi() <em>Bundles Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundlesApi()
	 * @generated
	 * @ordered
	 */
    protected static final TagBundlesApi BUNDLES_API_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getBundlesApi() <em>Bundles Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundlesApi()
	 * @generated
	 * @ordered
	 */
    protected TagBundlesApi bundlesApi = BUNDLES_API_EDEFAULT;

    /**
	 * The default value of the '{@link #getPostsApi() <em>Posts Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPostsApi()
	 * @generated
	 * @ordered
	 */
    protected static final PostsApi POSTS_API_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPostsApi() <em>Posts Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPostsApi()
	 * @generated
	 * @ordered
	 */
    protected PostsApi postsApi = POSTS_API_EDEFAULT;

    /**
	 * The default value of the '{@link #getTagsApi() <em>Tags Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTagsApi()
	 * @generated
	 * @ordered
	 */
    protected static final TagsApi TAGS_API_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTagsApi() <em>Tags Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTagsApi()
	 * @generated
	 * @ordered
	 */
    protected TagsApi tagsApi = TAGS_API_EDEFAULT;

    /**
	 * The default value of the '{@link #getUpdateApi() <em>Update Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateApi()
	 * @generated
	 * @ordered
	 */
    protected static final UpdateApi UPDATE_API_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUpdateApi() <em>Update Api</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateApi()
	 * @generated
	 * @ordered
	 */
    protected UpdateApi updateApi = UPDATE_API_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CacheImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DeliciousjCachePackage.Literals.CACHE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Post> getPosts() {
        if (posts == null) {
            posts = new EObjectContainmentEList<Post>(Post.class, this, DeliciousjCachePackage.CACHE__POSTS);
        }
        return posts;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Tag> getTags() {
        if (tags == null) {
            tags = new EObjectContainmentEList<Tag>(Tag.class, this, DeliciousjCachePackage.CACHE__TAGS);
        }
        return tags;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Bundle> getBundles() {
        if (bundles == null) {
            bundles = new EObjectContainmentEList<Bundle>(Bundle.class, this, DeliciousjCachePackage.CACHE__BUNDLES);
        }
        return bundles;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TagBundlesApi getBundlesApi() {
        return bundlesApi;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBundlesApi(TagBundlesApi newBundlesApi) {
        TagBundlesApi oldBundlesApi = bundlesApi;
        bundlesApi = newBundlesApi;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeliciousjCachePackage.CACHE__BUNDLES_API, oldBundlesApi, bundlesApi));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PostsApi getPostsApi() {
        return postsApi;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPostsApi(PostsApi newPostsApi) {
        PostsApi oldPostsApi = postsApi;
        postsApi = newPostsApi;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeliciousjCachePackage.CACHE__POSTS_API, oldPostsApi, postsApi));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TagsApi getTagsApi() {
        return tagsApi;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTagsApi(TagsApi newTagsApi) {
        TagsApi oldTagsApi = tagsApi;
        tagsApi = newTagsApi;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeliciousjCachePackage.CACHE__TAGS_API, oldTagsApi, tagsApi));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UpdateApi getUpdateApi() {
        return updateApi;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpdateApi(UpdateApi newUpdateApi) {
        UpdateApi oldUpdateApi = updateApi;
        updateApi = newUpdateApi;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeliciousjCachePackage.CACHE__UPDATE_API, oldUpdateApi, updateApi));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public Update update() throws ThrottledException, UnauthorizedException {
        Update update = getUpdateApi().update();
        PostsApi postsApi = getPostsApi();
        Collection<de.wadndadn.deliciousj.Post> posts = postsApi.recent(null, 3);
        EList<Post> resultPosts = DeliciousjCacheFactory.eINSTANCE.createPosts(getPostsApi(), getTagsApi(), posts);
        getPosts().addAll(resultPosts);
        TagsApi tagsApi = getTagsApi();
        Map<String, Integer> tags = tagsApi.get();
        EList<Tag> resultTags = DeliciousjCacheFactory.eINSTANCE.createTags(getTagsApi(), tags);
        getTags().addAll(resultTags);
        TagBundlesApi bundlesApi = getBundlesApi();
        Map<String, Collection<String>> bundles = bundlesApi.all();
        EList<Bundle> resultBundles = DeliciousjCacheFactory.eINSTANCE.createBundles(getBundlesApi(), getTagsApi(), bundles);
        getBundles().addAll(resultBundles);
        return update;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public EList<Suggest> suggest(URL url) throws ThrottledException, UnauthorizedException {
        Map<String, SuggestType> suggests = getPostsApi().suggest(url);
        EList<Suggest> resultSuggests = DeliciousjCacheFactory.eINSTANCE.createSuggests(suggests);
        return resultSuggests;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void checkConfiguration() throws ConfigurationException, ThrottledException, UnauthorizedException {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DeliciousjCachePackage.CACHE__POSTS:
                return ((InternalEList<?>) getPosts()).basicRemove(otherEnd, msgs);
            case DeliciousjCachePackage.CACHE__TAGS:
                return ((InternalEList<?>) getTags()).basicRemove(otherEnd, msgs);
            case DeliciousjCachePackage.CACHE__BUNDLES:
                return ((InternalEList<?>) getBundles()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DeliciousjCachePackage.CACHE__POSTS:
                return getPosts();
            case DeliciousjCachePackage.CACHE__TAGS:
                return getTags();
            case DeliciousjCachePackage.CACHE__BUNDLES:
                return getBundles();
            case DeliciousjCachePackage.CACHE__BUNDLES_API:
                return getBundlesApi();
            case DeliciousjCachePackage.CACHE__POSTS_API:
                return getPostsApi();
            case DeliciousjCachePackage.CACHE__TAGS_API:
                return getTagsApi();
            case DeliciousjCachePackage.CACHE__UPDATE_API:
                return getUpdateApi();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DeliciousjCachePackage.CACHE__POSTS:
                getPosts().clear();
                getPosts().addAll((Collection<? extends Post>) newValue);
                return;
            case DeliciousjCachePackage.CACHE__TAGS:
                getTags().clear();
                getTags().addAll((Collection<? extends Tag>) newValue);
                return;
            case DeliciousjCachePackage.CACHE__BUNDLES:
                getBundles().clear();
                getBundles().addAll((Collection<? extends Bundle>) newValue);
                return;
            case DeliciousjCachePackage.CACHE__BUNDLES_API:
                setBundlesApi((TagBundlesApi) newValue);
                return;
            case DeliciousjCachePackage.CACHE__POSTS_API:
                setPostsApi((PostsApi) newValue);
                return;
            case DeliciousjCachePackage.CACHE__TAGS_API:
                setTagsApi((TagsApi) newValue);
                return;
            case DeliciousjCachePackage.CACHE__UPDATE_API:
                setUpdateApi((UpdateApi) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case DeliciousjCachePackage.CACHE__POSTS:
                getPosts().clear();
                return;
            case DeliciousjCachePackage.CACHE__TAGS:
                getTags().clear();
                return;
            case DeliciousjCachePackage.CACHE__BUNDLES:
                getBundles().clear();
                return;
            case DeliciousjCachePackage.CACHE__BUNDLES_API:
                setBundlesApi(BUNDLES_API_EDEFAULT);
                return;
            case DeliciousjCachePackage.CACHE__POSTS_API:
                setPostsApi(POSTS_API_EDEFAULT);
                return;
            case DeliciousjCachePackage.CACHE__TAGS_API:
                setTagsApi(TAGS_API_EDEFAULT);
                return;
            case DeliciousjCachePackage.CACHE__UPDATE_API:
                setUpdateApi(UPDATE_API_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DeliciousjCachePackage.CACHE__POSTS:
                return posts != null && !posts.isEmpty();
            case DeliciousjCachePackage.CACHE__TAGS:
                return tags != null && !tags.isEmpty();
            case DeliciousjCachePackage.CACHE__BUNDLES:
                return bundles != null && !bundles.isEmpty();
            case DeliciousjCachePackage.CACHE__BUNDLES_API:
                return BUNDLES_API_EDEFAULT == null ? bundlesApi != null : !BUNDLES_API_EDEFAULT.equals(bundlesApi);
            case DeliciousjCachePackage.CACHE__POSTS_API:
                return POSTS_API_EDEFAULT == null ? postsApi != null : !POSTS_API_EDEFAULT.equals(postsApi);
            case DeliciousjCachePackage.CACHE__TAGS_API:
                return TAGS_API_EDEFAULT == null ? tagsApi != null : !TAGS_API_EDEFAULT.equals(tagsApi);
            case DeliciousjCachePackage.CACHE__UPDATE_API:
                return UPDATE_API_EDEFAULT == null ? updateApi != null : !UPDATE_API_EDEFAULT.equals(updateApi);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (bundlesApi: ");
        result.append(bundlesApi);
        result.append(", postsApi: ");
        result.append(postsApi);
        result.append(", tagsApi: ");
        result.append(tagsApi);
        result.append(", updateApi: ");
        result.append(updateApi);
        result.append(')');
        return result.toString();
    }
}

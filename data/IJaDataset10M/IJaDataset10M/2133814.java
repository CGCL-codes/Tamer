package org.gamegineer.table.internal.ui.services.cardpilebasedesignuiregistry;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import static org.gamegineer.common.core.runtime.Assert.assertStateLegal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.Icon;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Path;
import org.gamegineer.table.core.CardPileBaseDesignId;
import org.gamegineer.table.internal.ui.Activator;
import org.gamegineer.table.internal.ui.BundleConstants;
import org.gamegineer.table.internal.ui.Loggers;
import org.gamegineer.table.internal.ui.util.swing.IconProxy;
import org.gamegineer.table.ui.ICardPileBaseDesignUI;
import org.gamegineer.table.ui.TableUIFactory;
import org.gamegineer.table.ui.services.cardpilebasedesignuiregistry.ICardPileBaseDesignUIRegistry;
import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * A component that adapts card pile base design user interfaces published via
 * the {@code org.gamegineer.table.ui.cardPileBaseDesignUIs} extension point to
 * the card pile base design user interface registry.
 */
@ThreadSafe
public final class CardPileBaseDesignUIRegistryExtensionPointAdapter implements IRegistryEventListener {

    /**
     * The configuration element attribute specifying the card pile base design
     * icon.
     */
    private static final String ATTR_ICON = "icon";

    /**
     * The configuration element attribute specifying the card pile base design
     * identifier.
     */
    private static final String ATTR_ID = "id";

    /**
     * The configuration element attribute specifying the card pile base design
     * name.
     */
    private static final String ATTR_NAME = "name";

    /** The card pile base design user interface registry service. */
    @GuardedBy("lock_")
    private ICardPileBaseDesignUIRegistry cardPileBaseDesignUIRegistry_;

    /**
     * The collection of card pile base design user interfaces created from the
     * extension registry.
     */
    @GuardedBy("lock_")
    private Collection<CardPileBaseDesignUI> cardPileBaseDesignUIs_;

    /** The extension registry service. */
    @GuardedBy("lock_")
    private IExtensionRegistry extensionRegistry_;

    /** The instance lock. */
    private final Object lock_;

    /**
     * Initializes a new instance of the {@code
     * CardPileBaseDesignUIRegistryExtensionPointAdapter} class.
     */
    public CardPileBaseDesignUIRegistryExtensionPointAdapter() {
        lock_ = new Object();
        cardPileBaseDesignUIRegistry_ = null;
        cardPileBaseDesignUIs_ = new ArrayList<CardPileBaseDesignUI>();
        extensionRegistry_ = null;
    }

    /**
     * Activates this component.
     */
    public void activate() {
        registerCardPileBaseDesignUIs();
        extensionRegistry_.addListener(this, BundleConstants.CARD_PILE_BASE_DESIGN_UIS_EXTENSION_POINT_UNIQUE_ID);
    }

    @Override
    public void added(final IExtension[] extensions) {
        for (final IExtension extension : extensions) {
            for (final IConfigurationElement configurationElement : extension.getConfigurationElements()) {
                registerCardPileBaseDesignUI(configurationElement);
            }
        }
    }

    @Override
    public void added(@SuppressWarnings("unused") final IExtensionPoint[] extensionPoints) {
    }

    /**
     * Binds the card pile base design user interface registry service to this
     * component.
     * 
     * @param cardPileBaseDesignUIRegistry
     *        The card pile base design user interface registry service; must
     *        not be {@code null}.
     * 
     * @throws java.lang.IllegalStateException
     *         If the card pile base design user interface registry is already
     *         bound.
     * @throws java.lang.NullPointerException
     *         If {@code cardPileBaseDesignUIRegistry} is {@code null}.
     */
    public void bindCardPileBaseDesignUIRegistry(final ICardPileBaseDesignUIRegistry cardPileBaseDesignUIRegistry) {
        assertArgumentNotNull(cardPileBaseDesignUIRegistry, "cardPileBaseDesignUIRegistry");
        synchronized (lock_) {
            assertStateLegal(cardPileBaseDesignUIRegistry_ == null, Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_bindCardPileBaseDesignUIRegistry_bound);
            cardPileBaseDesignUIRegistry_ = cardPileBaseDesignUIRegistry;
        }
    }

    /**
     * Binds the extension registry service to this component.
     * 
     * @param extensionRegistry
     *        The extension registry service; must not be {@code null}.
     * 
     * @throws java.lang.IllegalStateException
     *         If the extension registry is already bound.
     * @throws java.lang.NullPointerException
     *         If {@code extensionRegistry} is {@code null}.
     */
    public void bindExtensionRegistry(final IExtensionRegistry extensionRegistry) {
        assertArgumentNotNull(extensionRegistry, "extensionRegistry");
        synchronized (lock_) {
            assertStateLegal(extensionRegistry_ == null, Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_bindExtensionRegistry_bound);
            extensionRegistry_ = extensionRegistry;
        }
    }

    private static CardPileBaseDesignUI createCardPileBaseDesignUI(final IConfigurationElement configurationElement) {
        assert configurationElement != null;
        final String idString = configurationElement.getAttribute(ATTR_ID);
        if (idString == null) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_missingId);
        }
        final CardPileBaseDesignId id = CardPileBaseDesignId.fromString(idString);
        final String name = configurationElement.getAttribute(ATTR_NAME);
        if (name == null) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_missingName);
        }
        final String iconPath = configurationElement.getAttribute(ATTR_ICON);
        if (iconPath == null) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_missingIconPath);
        }
        final PackageAdmin packageAdmin = Activator.getDefault().getPackageAdmin();
        if (packageAdmin == null) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_noPackageAdminService);
        }
        final Bundle[] bundles = packageAdmin.getBundles(configurationElement.getNamespaceIdentifier(), null);
        if ((bundles == null) || (bundles.length == 0)) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_iconBundleNotFound(configurationElement.getNamespaceIdentifier()));
        }
        final URL iconUrl = FileLocator.find(bundles[0], new Path(iconPath), null);
        if (iconUrl == null) {
            throw new IllegalArgumentException(Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_createCardPileBaseDesignUI_iconFileNotFound(bundles[0], iconPath));
        }
        final Icon icon = new IconProxy(iconUrl);
        return new CardPileBaseDesignUI(configurationElement.getDeclaringExtension(), id, name, icon);
    }

    /**
     * Deactivates this component.
     */
    public void deactivate() {
        extensionRegistry_.removeListener(this);
        unregisterCardPileBaseDesignUIs();
    }

    /**
     * Indicates the specified card pile base design user interface was
     * contributed by the specified extension.
     * 
     * @param cardPileBaseDesignUI
     *        The card pile base design user interface; must not be {@code null}
     *        .
     * @param extension
     *        The extension; must not be {@code null}.
     * 
     * @return {@code true} if the specified card pile base design user
     *         interface was contributed by the specified extension; otherwise
     *         {@code false}.
     */
    private static boolean isCardPileBaseDesignUIContributedByExtension(final CardPileBaseDesignUI cardPileBaseDesignUI, final IExtension extension) {
        assert cardPileBaseDesignUI != null;
        assert extension != null;
        if (!cardPileBaseDesignUI.getExtensionNamespaceId().equals(extension.getNamespaceIdentifier())) {
            return false;
        }
        final String extensionSimpleId = cardPileBaseDesignUI.getExtensionSimpleId();
        return (extensionSimpleId == null) || extensionSimpleId.equals(extension.getSimpleIdentifier());
    }

    /**
     * Registers the card pile base design user interface represented by the
     * specified extension configuration element.
     * 
     * @param configurationElement
     *        The extension configuration element; must not be {@code null}.
     */
    private void registerCardPileBaseDesignUI(final IConfigurationElement configurationElement) {
        assert configurationElement != null;
        final CardPileBaseDesignUI cardPileBaseDesignUI;
        try {
            cardPileBaseDesignUI = createCardPileBaseDesignUI(configurationElement);
        } catch (final IllegalArgumentException e) {
            Loggers.getDefaultLogger().log(Level.SEVERE, Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_registerCardPileBaseDesignUI_parseError(configurationElement.getAttribute(ATTR_ID)), e);
            return;
        }
        synchronized (lock_) {
            cardPileBaseDesignUIRegistry_.registerCardPileBaseDesignUI(cardPileBaseDesignUI);
            cardPileBaseDesignUIs_.add(cardPileBaseDesignUI);
        }
    }

    /**
     * Registers all card pile base design user interfaces in the extension
     * registry.
     */
    private void registerCardPileBaseDesignUIs() {
        for (final IConfigurationElement configurationElement : extensionRegistry_.getConfigurationElementsFor(BundleConstants.CARD_PILE_BASE_DESIGN_UIS_EXTENSION_POINT_UNIQUE_ID)) {
            registerCardPileBaseDesignUI(configurationElement);
        }
    }

    @Override
    public void removed(final IExtension[] extensions) {
        for (final IExtension extension : extensions) {
            unregisterCardPileBaseDesignUIs(extension);
        }
    }

    @Override
    public void removed(@SuppressWarnings("unused") final IExtensionPoint[] extensionPoints) {
    }

    /**
     * Unbinds the card pile base design user interface registry service from
     * this component.
     * 
     * @param cardPileBaseDesignUIRegistry
     *        The card pile base design user interface registry service; must
     *        not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code cardPileBaseDesignUIRegistry} is not currently bound.
     * @throws java.lang.NullPointerException
     *         If {@code cardPileBaseDesignUIRegistry} is {@code null}.
     */
    public void unbindCardPileBaseDesignUIRegistry(final ICardPileBaseDesignUIRegistry cardPileBaseDesignUIRegistry) {
        assertArgumentNotNull(cardPileBaseDesignUIRegistry, "cardPileBaseDesignUIRegistry");
        synchronized (lock_) {
            assertArgumentLegal(cardPileBaseDesignUIRegistry_ == cardPileBaseDesignUIRegistry, "cardPileBaseDesignUIRegistry", Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_unbindCardPileBaseDesignUIRegistry_notBound);
            cardPileBaseDesignUIRegistry_ = null;
        }
    }

    /**
     * Unbinds the extension registry service from this component.
     * 
     * @param extensionRegistry
     *        The extension registry service; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code extensionRegistry} is not currently bound.
     * @throws java.lang.NullPointerException
     *         If {@code extensionRegistry} is {@code null}.
     */
    public void unbindExtensionRegistry(final IExtensionRegistry extensionRegistry) {
        assertArgumentNotNull(extensionRegistry, "extensionRegistry");
        synchronized (lock_) {
            assertArgumentLegal(extensionRegistry_ == extensionRegistry, "extensionRegistry", Messages.CardPileBaseDesignUIRegistryExtensionPointAdapter_unbindExtensionRegistry_notBound);
            extensionRegistry_ = null;
        }
    }

    /**
     * Unregisters all card pile base design user interfaces.
     */
    private void unregisterCardPileBaseDesignUIs() {
        synchronized (lock_) {
            for (final CardPileBaseDesignUI cardPileBaseDesignUI : cardPileBaseDesignUIs_) {
                cardPileBaseDesignUIRegistry_.unregisterCardPileBaseDesignUI(cardPileBaseDesignUI);
            }
            cardPileBaseDesignUIs_.clear();
        }
    }

    /**
     * Unregisters all card pile base design user interfaces contributed by the
     * specified extension.
     * 
     * @param extension
     *        The extension; must not be {@code null}.
     */
    private void unregisterCardPileBaseDesignUIs(final IExtension extension) {
        assert extension != null;
        synchronized (lock_) {
            for (final Iterator<CardPileBaseDesignUI> iterator = cardPileBaseDesignUIs_.iterator(); iterator.hasNext(); ) {
                final CardPileBaseDesignUI cardPileBaseDesignUI = iterator.next();
                if (isCardPileBaseDesignUIContributedByExtension(cardPileBaseDesignUI, extension)) {
                    cardPileBaseDesignUIRegistry_.unregisterCardPileBaseDesignUI(cardPileBaseDesignUI);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Implementation of {@link org.gamegineer.table.ui.ICardPileBaseDesignUI}
     * created from an extension.
     */
    @Immutable
    private static final class CardPileBaseDesignUI implements ICardPileBaseDesignUI {

        /**
         * The card pile base design user interface to which all behavior is
         * delegated.
         */
        private final ICardPileBaseDesignUI delegate_;

        /** The namespace identifier of the contributing extension. */
        private final String extensionNamespaceId_;

        /** The simple identifier of the contributing extension. */
        private final String extensionSimpleId_;

        /**
         * Initializes a new instance of the {@code CardPileBaseDesignUI} class.
         * 
         * @param extension
         *        The extension that contributed this card pile base design user
         *        interface; must not be {@code null}.
         * @param id
         *        The card pile base design identifier; must not be {@code null}
         *        .
         * @param name
         *        The card pile base design name; must not be {@code null}.
         * @param icon
         *        The card pile base design icon; must not be {@code null}.
         * 
         * @throws java.lang.NullPointerException
         *         If {@code id}, {@code name}, or {@code icon} is {@code null}.
         */
        CardPileBaseDesignUI(final IExtension extension, final CardPileBaseDesignId id, final String name, final Icon icon) {
            assert extension != null;
            extensionNamespaceId_ = extension.getNamespaceIdentifier();
            extensionSimpleId_ = extension.getSimpleIdentifier();
            delegate_ = TableUIFactory.createCardPileBaseDesignUI(id, name, icon);
        }

        String getExtensionNamespaceId() {
            return extensionNamespaceId_;
        }

        String getExtensionSimpleId() {
            return extensionSimpleId_;
        }

        @Override
        public Icon getIcon() {
            return delegate_.getIcon();
        }

        @Override
        public CardPileBaseDesignId getId() {
            return delegate_.getId();
        }

        @Override
        public String getName() {
            return delegate_.getName();
        }
    }
}

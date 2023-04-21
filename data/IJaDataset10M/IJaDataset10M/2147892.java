package net.sf.amemailchecker.app.extension;

/**
 * Represents application extension.
 *
 * MANIFEST.MF keys:
 *
 * Required:
 * <code>Ame-Mail-Checker-extension-class</code> - fully qualified class name
 * <code>Ame-Mail-Checker-extension-name</code> - extension name
 * <code>Ame-Mail-Checker-extension-version</code> - extension version
 * <code>Ame-Mail-Checker-extension-min-required-app-version</code> - minimal target version of the host application in
 * format major.minor.revision (for example, 0.4.1)
 *
 * Optional:
 * <code>Ame-Mail-Checker-extension-author</code> - author name
 * <code>Ame-Mail-Checker-extension-email</code> - author email address
 * <code>Ame-Mail-Checker-extension-home-page</code> - extension homepage
 * <code>Ame-Mail-Checker-extension-description-bundle-key</code> - key in i18n bundle for description string
 * <code>Ame-Mail-Checker-extension-i18n-bundle</code> - full path to i18n bundle, including bundle name
 * (for example, org/resources/i18n/extension, where last chunk of the path is a bundle name)
 * <code>Ame-Mail-Checker-extension-icon-data</code> - full path to properties file with icons information, that mapped
 * on key <code>icon.data</code> and separated by ~ char.
 * Example:
 *
 * <code>icon.data = icon.one = org/example/resources/iconset/icon_one.png~\
 *    icon.two = org/example/resources/iconset/icon_two.png</code>
 *
 */
public interface Extension {

    void startup(ExtensionContext context) throws Exception;

    void shutdown();

    ExtensionPoint[] extensionPoints();

    ExtensionSettings settings();
}

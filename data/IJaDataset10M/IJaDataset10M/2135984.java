package net.confex.translations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class Translator {

    private static List<IMessages> messages_classes = new ArrayList<IMessages>();

    private static Translator translator = new Translator();

    public Translator() {
        messages_classes.add(new Messages());
        getMessagesFromExtensionPoints();
    }

    private static String curent_lang = "";

    /**
	 * ��������� ������������ ������
	 * @param key
	 * @return
	 */
    public static String getString(String key) {
        String s = null;
        for (Iterator<IMessages> iter = messages_classes.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            IMessages m = ((IMessages) obj);
            s = m.getString(key);
            if (s != null) return s;
        }
        if (s == null) {
            return '!' + key + '!';
        }
        return s;
    }

    /**
	 * ��������� �������� �����
	 * 
	 * Ex: Translator .setLang("ru");
	 * @param lang
	 */
    public static void setLang(String lang) {
        curent_lang = lang;
        for (Iterator<IMessages> iter = messages_classes.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            IMessages m = ((IMessages) obj);
            m.switchLocale(lang);
        }
    }

    public static String getLang() {
        return curent_lang;
    }

    private static ArrayList<ITranslatable> translatable_list = new ArrayList<ITranslatable>();

    public static void addTranslatable(ITranslatable translatable) {
        translatable_list.add(translatable);
    }

    public static void removeAllTranslatables() {
        translatable_list.clear();
    }

    /**
	 * ��������� ��� ������������ ������� � ��������� �����������
	 */
    public static void updateTranslatables() {
        for (int i = 0; i < translatable_list.size(); i++) {
            ((ITranslatable) translatable_list.get(i)).updateLang();
        }
    }

    /**
	 * ������� ������������ ������ �� ������ ����������
	 * @param translatable
	 */
    public static void removeTranslatable(ITranslatable translatable) {
        translatable_list.remove(translatable);
    }

    private void getMessagesFromExtensionPoints() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("net.confex.confexMessages");
        if (point == null) return;
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] elements = extensions[i].getConfigurationElements();
            for (int j = 0; j < elements.length; j++) {
                try {
                    Object obj = elements[j].createExecutableExtension("class");
                    if (obj instanceof IMessages) {
                        messages_classes.add((IMessages) obj);
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removeAllMessages() {
        messages_classes.clear();
    }
}

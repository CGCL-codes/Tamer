package library.util;

import library.Library;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;

/**
 * library system �� �ʿ��� utility static method ����
 * 
 * @author c0338027
 * 
 */
public class LibraryUtility {

    /**
	 * obj ���� root(library) ������
	 * 
	 * @param obj
	 * @return
	 */
    public static Library getRoot(EObject obj) {
        while (!(obj instanceof Library)) obj = obj.eContainer();
        return (Library) obj;
    }

    /**
	 * AddCommand ���� child �ϳ� ������
	 * 
	 * @param command
	 * @return
	 */
    public static EObject child(AddCommand command) {
        return (EObject) command.getCollection().iterator().next();
    }
}

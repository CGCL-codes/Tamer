package controller.persistence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.HibernateDAOFactory;
import model.dao.InterpreterClass;
import model.dao.InterpreterException;

/**
 * Classe que controle as intera��es com o form da manuten��o
 * 
 * @author Leandro
 * 
 */
public class MaintenanceAction implements ActionListener {

    private VisualMaintenanceAction vAction;

    public void actionPerformed(ActionEvent evt) {
        Class context = null;
        try {
            String action = evt.getActionCommand();
            context = InterpreterClass.getEntityClass(action);
            vAction = new VisualMaintenanceAction(new HibernateDAOFactory());
            vAction.startUp(context);
        } catch (InterpreterException e) {
            e.printStackTrace();
        }
    }
}

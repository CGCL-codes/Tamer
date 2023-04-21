package Bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author yassin
 */
@ManagedBean
@SessionScoped
public class MenuLateral {

    private String selected_m_item = "Usuários";

    /**
     * @return the selected_m_item
     */
    public String getSelected() {
        return selected_m_item;
    }

    /**
     * @param selected_m_item the selected_m_item to set
     */
    public void setSelected(String selected) {
        this.selected_m_item = selected;
    }

    public void option_selected(ActionEvent ev) {
        setSelected(ev.getComponent().getParent().getId());
    }
}

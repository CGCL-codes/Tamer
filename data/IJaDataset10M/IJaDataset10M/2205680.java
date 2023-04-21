package org.humboldt.cassia.zk.render;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.humboldt.cassia.core.jdo.RecursosUsuario;
import org.humboldt.cassia.core.jdo.Usuario;
import org.humboldt.cassia.zk.actions.ListenerAsociarRecurso;
import org.humboldt.cassia.zk.actions.UsuariosAction;
import org.humboldt.cassia.zk.model.RecursosUsuariosModel;
import org.humboldt.cassia.zk.model.UsuariosModel;
import org.humboldt.cassia.zk.util.Cache;
import org.humboldt.cassia.zk.util.SessionUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.api.Comboitem;

public class UsuariosListadoGridRenderer implements RowRenderer {

    private boolean eliminar;

    private boolean editar;

    private boolean log;

    private boolean seleccionar;

    private EventListener listenerSeleccion;

    private String etiquetaSeleccionar;

    public void render(Row row, Object data) throws Exception {
        if (data instanceof Usuario) {
            Usuario usuario = (Usuario) data;
            new Label(usuario.getNombres()).setParent(row);
            new Label(Cache.getTipoUsuario(usuario.getTipoUsuario()).getDescripcion()).setParent(row);
            if (editar) {
                colocarBotonEdicion(row, usuario);
            } else if (eliminar) {
                colocarBotonEliminar(row, usuario);
            } else if (log) {
                colocarBotonLog(row, usuario);
            } else if (seleccionar) {
                colocarBotonSeleccionar(row, usuario);
            }
        } else if (data instanceof String) {
            new Label((String) data).setParent(row);
        } else {
            System.out.println("PASO");
        }
    }

    private void colocarBotonEdicion(Row row, Usuario usuario) {
        Button button = new Button(Labels.getLabel("btn_usuario_edit"));
        button.setParent(row);
        button.setAttribute("usuario", usuario);
        button.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
                Component componente = event.getTarget();
                componente.getRoot().getFellow("tab_edit").setVisible(true);
                ((Tab) componente.getRoot().getFellow("tab_edit")).setSelected(true);
                Usuario usuario = (Usuario) componente.getAttribute("usuario");
                usuario = SessionUtil.getFacadeConsultasUsuarioCassiaCore().consultarUsuario(usuario.getId());
                Button btnGuardar = ((Button) componente.getRoot().getFellow("btnGuardarUsuario"));
                btnGuardar.setAttribute("usuario", usuario);
                Button btnReasignar = (Button) componente.getRoot().getFellow("btnAsignarRecursos");
                btnReasignar.setAttribute("usuario", usuario);
                btnReasignar.addEventListener(Events.ON_CLICK, new ListenerAsociarRecurso());
                ((Textbox) componente.getRoot().getFellow("login")).setText(usuario.getLogin());
                ((Textbox) componente.getRoot().getFellow("nombres")).setText(usuario.getNombres());
                ((Textbox) componente.getRoot().getFellow("numeroid")).setText(usuario.getNumerodoc());
                ((Textbox) componente.getRoot().getFellow("direccion")).setText(usuario.getDireccion());
                ((Textbox) componente.getRoot().getFellow("telefono")).setText(usuario.getTelefono());
                List tiposDoc = ((Combobox) componente.getRoot().getFellow("tipodoc")).getChildren();
                int i = 0;
                for (Iterator itTiposDoc = tiposDoc.iterator(); itTiposDoc.hasNext(); ) {
                    Comboitem item = (Comboitem) itTiposDoc.next();
                    if (usuario.getTipodoc() == Long.parseLong((String) item.getValue())) {
                        ((Combobox) componente.getRoot().getFellow("tipodoc")).setSelectedIndex(i);
                        break;
                    }
                    i++;
                }
                List tiposUsuario = ((Combobox) componente.getRoot().getFellow("perfil")).getChildren();
                i = 0;
                for (Iterator itTiposUsu = tiposUsuario.iterator(); itTiposUsu.hasNext(); ) {
                    Comboitem item = (Comboitem) itTiposUsu.next();
                    if (usuario.getTipoUsuario() == Long.parseLong((String) item.getValue())) {
                        ((Combobox) componente.getRoot().getFellow("perfil")).setSelectedIndex(i);
                        break;
                    }
                    i++;
                }
                List entidades = ((Combobox) componente.getRoot().getFellow("entidad")).getChildren();
                i = 0;
                for (Iterator itTipoEntidad = entidades.iterator(); itTipoEntidad.hasNext(); ) {
                    Comboitem item = (Comboitem) itTipoEntidad.next();
                    if (usuario.getEntidad() == Long.parseLong((String) item.getValue())) {
                        ((Combobox) componente.getRoot().getFellow("entidad")).setSelectedIndex(i);
                        break;
                    }
                    i++;
                }
                ((Textbox) componente.getRoot().getFellow("email")).setText(usuario.getEmail());
                if (usuario.getPassword() != null && usuario.getPassword().length() > 0) ((Textbox) componente.getRoot().getFellow("clave")).setText(usuario.getPassword());
                ((Radio) componente.getRoot().getFellow("estadoAct")).setChecked(usuario.isActivo());
                ((Radio) componente.getRoot().getFellow("estadoIna")).setChecked(!usuario.isActivo());
                btnGuardar.addEventListener(Events.ON_CLICK, new EventListener() {

                    public void onEvent(Event event) throws Exception {
                        Component componente = event.getTarget();
                        new UsuariosAction().registarActualizarUsuario(componente);
                    }
                });
            }
        });
    }

    private void colocarBotonEliminar(Row row, Usuario usuario) {
        Collection<RecursosUsuario> cllRecursos = SessionUtil.getFacadeConsultasUsuarioCassiaCore().consultarRecursosUsuario(usuario.getId());
        if (cllRecursos != null && cllRecursos.size() > 0) {
            Button button = new Button(Labels.getLabel("btn_usuario_reasignar_recursos"));
            button.setParent(row);
            button.setAttribute("usuario", usuario);
            button.addEventListener(Events.ON_CLICK, new EventListener() {

                public void onEvent(Event event) throws Exception {
                    Component componente = event.getTarget();
                    Usuario usuario = (Usuario) componente.getAttribute("usuario");
                    String label = ((Label) componente.getRoot().getFellow("lblreasignarrecurso")).getValue();
                    label += " " + usuario.getNombres();
                    ((Label) componente.getRoot().getFellow("lblreasignarrecurso")).setValue(label);
                    componente.getRoot().getFellow("btnGuardar").setAttribute("usuario", usuario);
                    RecursosSeleccionRender arbolRenderSel = new RecursosSeleccionRender();
                    ((Tree) componente.getRoot().getFellow("arbolRecursos")).setModel(new RecursosUsuariosModel(usuario));
                    ((Tree) componente.getRoot().getFellow("arbolRecursos")).setTreeitemRenderer(arbolRenderSel);
                    componente.getRoot().getFellow("tab_recurso").setVisible(true);
                    ((Tab) componente.getRoot().getFellow("tab_recurso")).setSelected(true);
                }
            });
        } else {
            Button button = new Button(Labels.getLabel("btn_usuario_eliminar"));
            button.setParent(row);
            button.setAttribute("usuario", usuario);
            button.addEventListener(Events.ON_CLICK, new EventListener() {

                public void onEvent(Event event) throws Exception {
                    Component componente = event.getTarget();
                    Usuario usuario = (Usuario) componente.getAttribute("usuario");
                    SessionUtil.getFacadeOperacionesUsuarioCassiaCore().eliminarUsuario(usuario);
                    Messagebox.show(Labels.getLabel("msg_usuario_eliminado_ok"));
                }
            });
        }
    }

    private void colocarBotonLog(Row row, Usuario usuario) {
        Button button = new Button(Labels.getLabel("btn_usuario_log"));
        button.setParent(row);
        button.setAttribute("usuario", usuario);
        button.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) throws Exception {
                Component componente = event.getTarget();
                Usuario usuario = (Usuario) componente.getAttribute("usuario");
                usuario = SessionUtil.getFacadeConsultasUsuarioCassiaCore().consultarUsuario(usuario.getId());
                ((Grid) componente.getRoot().getFellow("gridLog")).setModel(new UsuariosModel().getLogUsuarioGroupGridModel(usuario));
                ((Grid) componente.getRoot().getFellow("gridLog")).setRowRenderer(new LogAuditoriaGridRenderer());
                componente.getRoot().getFellow("tab_log").setVisible(true);
                ((Tab) componente.getRoot().getFellow("tab_log")).setSelected(true);
            }
        });
    }

    private void colocarBotonSeleccionar(Row row, Usuario usuario) {
        Button button = new Button(etiquetaSeleccionar);
        button.setParent(row);
        button.setAttribute("usuario", usuario);
        button.addEventListener(Events.ON_CLICK, listenerSeleccion);
    }

    /**
	 * @return the eliminar
	 */
    public boolean isEliminar() {
        return eliminar;
    }

    /**
	 * @param eliminar the eliminar to set
	 */
    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    /**
	 * @return the editar
	 */
    public boolean isEditar() {
        return editar;
    }

    /**
	 * @param editar the editar to set
	 */
    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    /**
	 * @return the log
	 */
    public boolean isLog() {
        return log;
    }

    /**
	 * @param log the log to set
	 */
    public void setLog(boolean log) {
        this.log = log;
    }

    /**
	 * @return the seleccionar
	 */
    public boolean isSeleccionar() {
        return seleccionar;
    }

    /**
	 * @param seleccionar the seleccionar to set
	 */
    public void setSeleccionar(boolean seleccionar) {
        this.seleccionar = seleccionar;
    }

    /**
	 * @return the listenerSeleccion
	 */
    public EventListener getListenerSeleccion() {
        return listenerSeleccion;
    }

    /**
	 * @param listenerSeleccion the listenerSeleccion to set
	 */
    public void setListenerSeleccion(EventListener listenerSeleccion) {
        this.listenerSeleccion = listenerSeleccion;
    }

    /**
	 * @return the etiquetaSeleccionar
	 */
    public String getEtiquetaSeleccionar() {
        return etiquetaSeleccionar;
    }

    /**
	 * @param etiquetaSeleccionar the etiquetaSeleccionar to set
	 */
    public void setEtiquetaSeleccionar(String etiquetaSeleccionar) {
        this.etiquetaSeleccionar = etiquetaSeleccionar;
    }
}

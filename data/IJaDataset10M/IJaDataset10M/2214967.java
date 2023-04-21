package org.digitall.apps.resourcescontrol_091.interfaces.resourcesadmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import org.digitall.apps.personalfiles.classes.Dependencia;
import org.digitall.common.personalfiles.classes.Persona;
import org.digitall.apps.personalfiles.interfaces.PersonalPanel;
import org.digitall.apps.resourcescontrol.classes.HistorialRecursoDistinguible;
import org.digitall.apps.resourcescontrol_091.classes.DistinguishableResources;
import org.digitall.apps.resourcescontrol_091.classes.ResourcesDependency;
import org.digitall.apps.resourcescontrol_091.classes.ResourcesPerson;
import org.digitall.common.resourcescontrol.classes.Resource;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.sql.LibSQL;

public class DesAsignacionPatrimonioRecursoPanel extends BasicPrimitivePanel {

    private BasicPanel bpCentro = new BasicPanel();

    private TFInput tfiNombreRecurso = new TFInput(DataTypes.STRING, "Resource", false);

    private TFInput tfFechaIni = new TFInput(DataTypes.DATE, "AcquisitionDate", false);

    private TFInput tfFechaFin = new TFInput(DataTypes.DATE, "EndDate", false);

    private TFInput tfiMotivo = new TFInput(DataTypes.STRING, "Motivo", false);

    private TFInput tfDesgaste = new TFInput(DataTypes.DOUBLE, "Desgaste", false);

    private TFInput tfiToAssigned;

    private SaveDataButton btnSave = new SaveDataButton();

    private CloseButton btnClose = new CloseButton();

    private int idPerson = -1;

    private int idDependencia = -1;

    private int idResource;

    private HistorialRecursoDistinguible historialRecursoDistinguible = null;

    private RecursosVariosPanel recursosVariosPanel;

    private PersonalPanel personalPanel;

    private ListadoRecursosPatrimonialesDesAsignPanel listadoRecursosPatrimonialesDesAsignPanel;

    private Vector idsRecursosAsignar = new Vector();

    private Vector datosPersona;

    private Resource recurso;

    private Persona persona;

    private ResourcesPerson resourcesPerson;

    private DistinguishableResources distingushableResources;

    private Dependencia dependencia;

    private ResourcesDependency resourcesDependency;

    private boolean isPersonAssing;

    public DesAsignacionPatrimonioRecursoPanel(boolean _isPersonAssing) {
        try {
            isPersonAssing = _isPersonAssing;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        System.out.println("es asignacion a persona?: " + isPersonAssing);
        this.setSize(new Dimension(500, 303));
        this.setPreferredSize(new Dimension(500, 400));
        setTextToAssigned();
        bpCentro.setLayout(null);
        bpCentro.setBorder(BorderPanel.getBorderPanel("Agregar asignaci�n de recurso patrimonial"));
        tfiNombreRecurso.setBounds(new Rectangle(20, 80, 300, 35));
        tfiNombreRecurso.setEnabled(false);
        tfFechaIni.setBounds(new Rectangle(20, 190, 105, 35));
        tfFechaFin.setBounds(new Rectangle(140, 190, 105, 35));
        tfiMotivo.setBounds(new Rectangle(20, 165, 460, 35));
        tfDesgaste.setBounds(new Rectangle(20, 280, 100, 35));
        tfFechaIni.setBounds(new Rectangle(20, 125, 105, 35));
        tfFechaFin.setBounds(new Rectangle(135, 125, 105, 35));
        tfDesgaste.setBounds(new Rectangle(20, 210, 100, 35));
        tfiToAssigned.setBounds(new Rectangle(20, 35, 335, 35));
        tfiToAssigned.setEnabled(false);
        bpCentro.add(tfiToAssigned, null);
        bpCentro.add(tfDesgaste, null);
        bpCentro.add(tfiMotivo, null);
        bpCentro.add(tfFechaFin, null);
        bpCentro.add(tfFechaIni, null);
        bpCentro.add(tfiNombreRecurso, null);
        this.add(bpCentro, BorderLayout.CENTER);
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSave_actionPerformed(e);
            }
        });
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        this.addButton(btnClose);
        this.addButton(btnSave);
    }

    private void setTextToAssigned() {
        if (isPersonAssing) {
            tfiToAssigned = new TFInput(DataTypes.STRING, "Person", false);
        } else {
            tfiToAssigned = new TFInput(DataTypes.STRING, "Dependencia", false);
        }
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        getParentInternalFrame().close();
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        grabarAsignacionPatrimonial();
    }

    private void grabarAsignacionPatrimonial() {
        String mensajeError = pasoControles().elementAt(0).toString();
        String mensajeGeneracionPatrimonio = "";
        boolean controlOk = Boolean.parseBoolean(pasoControles().elementAt(1).toString());
        if (controlOk) {
            Vector sinPatrimonios = existResourcesDistinguishableNonPat();
            int cantRecursosSinPatrimonios = sinPatrimonios.size();
            if (cantRecursosSinPatrimonios > 0) {
                boolean respuesta = Advisor.question("Generacion Patrimonio: ", "Existen " + cantRecursosSinPatrimonios + "recursos sin Numero de Patrimonio \nDesea generar Numeros de Patrimonio?");
                if (respuesta) {
                    if (!generarNumerosPatrimonios(sinPatrimonios)) {
                        mensajeGeneracionPatrimonio = "y no se pudo generar los numeros de patrimonios";
                        Advisor.messageBox("No se pudo generar los numeros de patrimonio", "Error");
                    } else {
                        mensajeGeneracionPatrimonio = "y se gener� numero de patrimonio para " + cantRecursosSinPatrimonios + " recurso/s distinguible/s";
                    }
                }
            }
            boolean check = false;
            if (isPersonAssing) {
                check = saveDataResourcesPerson();
            } else {
                check = saveDataResourcesDependency();
            }
            if (check) {
                Advisor.messageBox("Se grabo con exito la asignacion de rescursos " + mensajeGeneracionPatrimonio, "Aviso");
                listadoRecursosPatrimonialesDesAsignPanel.getRecursosVariosPanel().refresh();
                listadoRecursosPatrimonialesDesAsignPanel.getParentInternalFrame().close();
                getParentInternalFrame().close();
            } else {
                Advisor.messageBox("No se pudo grabar asignacion de recursos", "Error");
            }
        } else {
            Advisor.messageBox(mensajeError, "Error");
        }
    }

    private Vector existResourcesDistinguishableNonPat() {
        Vector resultado = new Vector();
        Vector seleccionados = listadoRecursosPatrimonialesDesAsignPanel.getGrillaPanelPatrimonio().getSelectedsVector();
        int hasta = seleccionados.size();
        for (int i = 0; i < hasta; i++) {
            Vector filaRecursoDist = (Vector) seleccionados.elementAt(i);
            String numPatrimonio = "" + filaRecursoDist.elementAt(8).toString();
            if (numPatrimonio.equals("")) {
                resultado.add((Vector) filaRecursoDist);
            }
        }
        return (resultado);
    }

    private boolean generarNumerosPatrimonios(Vector _sinPatrimonios) {
        boolean resultado = false;
        Vector sinPatrimonios = _sinPatrimonios;
        int hasta = sinPatrimonios.size();
        for (int i = 0; i < hasta; i++) {
            Vector recurso = (Vector) sinPatrimonios.elementAt(i);
            int idRecursoDist = Integer.parseInt("0" + recurso.elementAt(0));
            String params = "" + idRecursoDist;
            if (LibSQL.getInt("resourcescontrol.addresourcepatrimonio", params) != idRecursoDist) {
                resultado = false;
            } else {
                resultado = true;
            }
        }
        return (resultado);
    }

    private Vector pasoControles() {
        Vector resultado = new Vector();
        String mjeError = "";
        boolean control = true;
        resultado.add(mjeError);
        resultado.add(control);
        return (resultado);
    }

    private boolean saveDataResourcesPerson() {
        boolean resultado = false;
        asignarRecursosDistinguiblesAPersonas();
        grabarMovimientos();
        return (true);
    }

    private boolean saveDataResourcesDependency() {
        boolean resultado = false;
        System.out.println("asignando recurso a dependencia: " + asignarRecursoDependencia());
        asignarRecursosDistinguiblesADependencia();
        idPerson = -1;
        grabarMovimientos();
        return (true);
    }

    private boolean asignarRecursosDistinguiblesADependencia() {
        boolean resultado = false;
        int idPerson = -1;
        int hasta = idsRecursosAsignar.size();
        for (int i = 0; i < hasta; i++) {
            distingushableResources = new DistinguishableResources();
            distingushableResources.setIdDistinguishableResource(Integer.parseInt(idsRecursosAsignar.elementAt(i).toString()));
            distingushableResources.retrieveData();
            distingushableResources.setAquisitionDate(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
            distingushableResources.setIdPerson(idPerson);
            if (distingushableResources.saveData() != -1) {
                resultado = true;
            }
        }
        return (resultado);
    }

    private int asignarRecursoDependencia() {
        int resultado = -1;
        int cantidadAsignada = idsRecursosAsignar.size();
        resourcesDependency = new ResourcesDependency();
        resourcesDependency.setIdDependency(dependencia.getIdDep());
        resourcesDependency.setIdDistResource(recurso.getIdResource());
        resourcesDependency.setDateAssigned(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
        resourcesDependency.setSockAssigned(cantidadAsignada);
        resultado = resourcesDependency.saveData();
        return (resultado);
    }

    private int asignarRecursoPersona() {
        int resultado = -1;
        resourcesPerson = new ResourcesPerson();
        int cantidadAsignada = idsRecursosAsignar.size();
        int idPersonaResponsable = persona.getIdPerson();
        int idRecurso = recurso.getIdResource();
        resourcesPerson.setIdPerson(idPersonaResponsable);
        resourcesPerson.setidDistResource(idRecurso);
        resourcesPerson.setDateAssigned(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
        resourcesPerson.setStockassigned(cantidadAsignada);
        resultado = resourcesPerson.saveData();
        return (resultado);
    }

    private boolean asignarRecursosDistinguiblesAPersonas() {
        boolean resultado = false;
        int idPerson = persona.getIdPerson();
        int hasta = idsRecursosAsignar.size();
        for (int i = 0; i < hasta; i++) {
            distingushableResources = new DistinguishableResources();
            distingushableResources.setIdDistinguishableResource(Integer.parseInt(idsRecursosAsignar.elementAt(i).toString()));
            distingushableResources.retrieveData();
            distingushableResources.setAquisitionDate(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
            distingushableResources.setIdPerson(idPerson);
            resourcesPerson.setIdPerson(idPerson);
            resourcesPerson.setidDistResource(Integer.parseInt(idsRecursosAsignar.elementAt(i).toString()));
            resourcesPerson.setDateAssigned(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
            resourcesPerson.setStockassigned(1);
            resourcesPerson.saveData();
            if (distingushableResources.saveData() != -1) {
                resultado = true;
            }
        }
        return (resultado);
    }

    private boolean grabarMovimientos() {
        boolean resultado = false;
        Vector seleccionados = idsRecursosAsignar;
        int hasta = seleccionados.size();
        int idRecursoDistinguible;
        for (int i = 0; i < hasta; i++) {
            historialRecursoDistinguible = new HistorialRecursoDistinguible();
            idRecursoDistinguible = Integer.parseInt(seleccionados.elementAt(i).toString());
            historialRecursoDistinguible.setIdDistinguishableResource(idRecursoDistinguible);
            historialRecursoDistinguible.setIdPerson(idPerson);
            historialRecursoDistinguible.setInitDate(Proced.setFormatDate(tfFechaIni.getDate().toString(), false));
            historialRecursoDistinguible.setEndDate(Proced.setFormatDate(tfFechaFin.getDate().toString(), false));
            historialRecursoDistinguible.setSubject(tfiMotivo.getString());
            historialRecursoDistinguible.setWaste(Double.parseDouble(tfDesgaste.getString()));
            historialRecursoDistinguible.setIdDependency(dependencia.getIdDep());
            int result = historialRecursoDistinguible.saveData();
        }
        return (true);
    }

    public void setIdPerson(int _idPerson) {
        idPerson = _idPerson;
    }

    public void setIdResurce(int _idResource) {
        idResource = _idResource;
    }

    public void setIdDependencia(int _idDependencia) {
        idDependencia = _idDependencia;
    }

    public void setNombreDependencia(String _nombreDependencia) {
        tfiToAssigned.setValue(_nombreDependencia);
    }

    public void setNombrePersona(String _nombrePersona) {
        tfiToAssigned.setValue(_nombrePersona);
    }

    public void setNombreRecurso(String _nombreRecurso) {
        tfiNombreRecurso.setValue(_nombreRecurso);
    }

    public void setRecursosVariosPanel(RecursosVariosPanel _recursosVariosPanel) {
        recursosVariosPanel = _recursosVariosPanel;
    }

    public void setPersonalPanel(PersonalPanel _personalPanel) {
        personalPanel = _personalPanel;
    }

    public void loadDataRecurso() {
        if (isPersonAssing) {
            persona = new Persona();
            persona.setIdPerson(idPerson);
            persona.retrieveData();
        } else {
            idPerson = -1;
        }
        int idRecurso = Integer.parseInt(listadoRecursosPatrimonialesDesAsignPanel.getRecursosVariosPanel().getGrillaPanelRecursos().getSelectedsID().elementAt(0).toString());
        recurso = new Resource();
        recurso.setIdResource(idRecurso);
        recurso.retrieveData();
        recurso = new Resource();
        recurso.setIdResource(idRecurso);
        recurso.retrieveData();
        String nombreRecurso = recurso.getName();
        if (isPersonAssing) {
            datosPersona = (Vector) (personalPanel.getGridPanelPersonal().getSelectedsVector().elementAt(0));
            dependencia = new Dependencia();
            String nombrePersona = datosPersona.elementAt(4).toString();
            tfiToAssigned.setValue(nombrePersona);
        } else {
            dependencia = listadoRecursosPatrimonialesDesAsignPanel.getDependencia();
            tfiToAssigned.setValue(dependencia.getNombre());
        }
        tfiNombreRecurso.setValue(nombreRecurso);
    }

    public void setListadoRecursosPatrimonialesDesAsignPanel(ListadoRecursosPatrimonialesDesAsignPanel listadoRecursosPatrimonialesDesAsignPanel) {
        this.listadoRecursosPatrimonialesDesAsignPanel = listadoRecursosPatrimonialesDesAsignPanel;
    }

    public ListadoRecursosPatrimonialesDesAsignPanel getListadoRecursosPatrimonialesPanel() {
        return listadoRecursosPatrimonialesDesAsignPanel;
    }

    public void setRecursosAsignar(Vector recursosAsignar) {
        this.idsRecursosAsignar = recursosAsignar;
    }

    public Vector getRecursosAsignar() {
        return idsRecursosAsignar;
    }
}

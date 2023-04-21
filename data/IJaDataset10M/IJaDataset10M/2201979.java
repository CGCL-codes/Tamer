package com.siegre.action;

import com.siegre.bean.BordeBean;
import com.siegre.model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.Length;
import org.hibernate.validator.Past;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.PasswordHash;
import org.jboss.seam.util.AnnotatedBeanProperty;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * Clase personauniversidadHome, session bean que implementa la logica de negocio para la entity personauniversidad
 * inserta, actualiza, consulta y elimina regsitro de la tabla
 * además contiene métodos con comportamientos específicos
 *
 * @author Rafael Ortega
 * @author Mayer Monsalve
 * @version 1.0 Build 10 Diciembre 15, 2011
 */
@Name("personauniversidadHome")
@Scope(ScopeType.CONVERSATION)
public class PersonauniversidadHome extends EntityHome<Personauniversidad> {

    private static final long serialVersionUID = 8542256852495176859L;

    @In(create = true)
    UsuarioHome usuarioHome;

    @In(create = true)
    PersonageneralHome personageneralHome;

    @In(create = true)
    PersonanaturalHome personanaturalHome;

    @In(create = true)
    MatriculaHome matriculaHome;

    @In
    EntityManager entityManager;

    @In
    private FacesMessages facesMessages;

    @Logger
    private Log log;

    @In
    DatosEmail datosEmail;

    private String selectedTab;

    private String mensaje;

    private String documento;

    private Date fechaNacimiento;

    private String email;

    private String emailConfirmacion;

    private String nombreUsuario;

    private String clave;

    @Length(min = 7, max = 7, message = "debe contener 7 digitos")
    private String codigoAlumno;

    private int indexLista;

    private List<Matricula> listaMatricula = new ArrayList<Matricula>();

    private boolean swLista = true;

    /** Metodo sobrescrito de la clase EntityHome - persist()
     * prepara el entity
     * inserta un registro en la tabla
     * coloca un facesMessage si la insercion fue exitosa
     *
     * @return String contiene "persisted" si fue exitoso
     */
    @Override
    public String persist() {
        try {
            personanaturalHome.persist();
            getInstance().preparar(personanaturalHome.getInstance());
            String exit = super.persist();
            insertarMatricula(listaMatricula);
            facesMessages.clear();
            facesMessages.add("Agregación Exitosa");
            return exit;
        } catch (InvalidStateException e) {
            for (InvalidValue invalidValue : e.getInvalidValues()) {
                log.info("Instancia del Entity Bean: " + invalidValue.getBeanClass().getSimpleName() + "\ntiene un propiedad invalida: " + invalidValue.getPropertyName() + "\ncon el mensaje: " + invalidValue.getMessage());
            }
        }
        return "error";
    }

    /** Metodo sobrescrito de la clase EntityHome - update()
     * prepara el entity
     * actualiza un registro en la tabla
     * coloca un facesMessage si la actualizacion fue exitosa
     *
     * @return String contiene "updated" si fue exitoso
     */
    @Override
    public String update() {
        try {
            personanaturalHome.update();
            getInstance().preparar();
            List<Matricula> listaMatriculaBackup = new ArrayList<Matricula>(listaMatricula);
            List<Matricula> listaMatriculaOriginal = new ArrayList<Matricula>(getInstance().getMatriculas());
            Iterator<Matricula> it = listaMatriculaOriginal.iterator();
            while (it.hasNext()) {
                Matricula matricula = (Matricula) it.next();
                Iterator<Matricula> it2 = listaMatricula.iterator();
                while (it2.hasNext()) {
                    Matricula matricula2 = (Matricula) it2.next();
                    if (matricula.getMatriId().equals(matricula2.getMatriId())) {
                        it.remove();
                        it2.remove();
                        break;
                    }
                }
            }
            if (!listaMatriculaOriginal.isEmpty()) {
                for (Matricula matri : listaMatriculaOriginal) entityManager.remove(matri);
                entityManager.flush();
            }
            if (!listaMatricula.isEmpty()) insertarMatricula(listaMatricula);
            listaMatricula = listaMatriculaBackup;
            String exit = super.update();
            facesMessages.clear();
            facesMessages.add("Edición Exitosa");
            return exit;
        } catch (InvalidStateException e) {
            for (InvalidValue invalidValue : e.getInvalidValues()) {
                log.info("Instancia del Entity Bean: " + invalidValue.getBeanClass().getSimpleName() + "\ntiene un propiedad invalida: " + invalidValue.getPropertyName() + "\ncon el mensaje: " + invalidValue.getMessage());
            }
        }
        return "error";
    }

    /** Metodo sobrescrito de la clase EntityHome - remove()
     * borra un registro en la tabla
     * coloca un facesMessage si el borrado fue exitoso
     *
     * @return String contiene "removed" si fue exitoso
     */
    @Override
    public String remove() {
        List<Matricula> listaMatriculaOriginal = new ArrayList<Matricula>(getInstance().getMatriculas());
        if (!listaMatriculaOriginal.isEmpty()) {
            for (Matricula matri : listaMatriculaOriginal) entityManager.remove(matri);
            entityManager.flush();
        }
        super.remove();
        String exit = personanaturalHome.remove();
        facesMessages.clear();
        facesMessages.add("Eliminación Exitosa");
        return exit;
    }

    public void setPersonauniversidadPeruId(Long id) {
        setId(id);
    }

    public Long getPersonauniversidadPeruId() {
        return (Long) getId();
    }

    @Override
    protected Personauniversidad createInstance() {
        Personauniversidad personauniversidad = new Personauniversidad();
        return personauniversidad;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
        Personanatural personanatural = personanaturalHome.getDefinedInstance();
        if (personanatural != null) getInstance().setPersonanatural(personanatural);
    }

    public boolean isWired() {
        if (getInstance().getPersonanatural() == null) return false;
        return true;
    }

    public Personauniversidad getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Matricula> getMatriculas() {
        return getInstance() == null ? null : new ArrayList<Matricula>(getInstance().getMatriculas());
    }

    public List<EncuestaGrande> getEncuestaGrandes() {
        return getInstance() == null ? null : new ArrayList<EncuestaGrande>(getInstance().getEncuestaGrandes());
    }

    public List<EncuestaPequenia> getEncuestaPequenias() {
        return getInstance() == null ? null : new ArrayList<EncuestaPequenia>(getInstance().getEncuestaPequenias());
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Past
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailConfirmacion() {
        return emailConfirmacion;
    }

    public void setEmailConfirmacion(String emailConfirmacion) {
        this.emailConfirmacion = emailConfirmacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public void setCodigoAlumno(String codigoAlumno) {
        this.codigoAlumno = codigoAlumno;
    }

    public Personageneral existePersonageneral(String pegeDocumento, long tiidId) {
        Personageneral pege = null;
        try {
            pege = (Personageneral) entityManager.createQuery("select personageneral from Personageneral personageneral where personageneral.pegeDocumento=:docu and personageneral.tipoidentificacion.tiidId=:tiidId").setParameter("docu", pegeDocumento).setParameter("tiidId", tiidId).getSingleResult();
            return pege;
        } catch (NoResultException nre) {
            return pege;
        }
    }

    public String verificarGraduando() {
        try {
            Alumno alumno = null;
            DatosAlumno datosAlumno = null;
            Tipoidentificacion tipoidentificacion = null;
            Carrera carrera = null;
            String password = Password.getPassword();
            String registradoPor = "externo";
            Date fechaCambio = new Date();
            String codCarrera = codigoAlumno.substring(0, 3);
            String codAlumno = codigoAlumno.substring(3, 7);
            byte opcion = 0;
            try {
                alumno = (Alumno) entityManager.createQuery("select alumno from Alumno alumno where alumno.codAlumno = ? and alumno.codCarrera = ?").setParameter(1, codAlumno).setParameter(2, codCarrera).getSingleResult();
                datosAlumno = (DatosAlumno) entityManager.createQuery("select datosAlumno from DatosAlumno datosAlumno where datosAlumno.documento=:docu").setParameter("docu", alumno.getDocumento()).getSingleResult();
                tipoidentificacion = (Tipoidentificacion) entityManager.createQuery("select tipoidentificacion from Tipoidentificacion tipoidentificacion where lower(tipoidentificacion.tiidAbreviatura)=:ab").setParameter("ab", datosAlumno.getTipoDocumento().toLowerCase()).getSingleResult();
                carrera = (Carrera) entityManager.createQuery("select carrera from Carrera carrera where carrera.carrCodigo=:codigo").setParameter("codigo", alumno.getCodCarrera()).getSingleResult();
            } catch (NoResultException e) {
                System.out.println("no existe registro");
            }
            if (alumno == null) {
                facesMessages.add("No se han encontrado registros con la informacion suministrada");
                mensaje = "No se han encontrado registros con la informacion suministrada.";
                return "error";
            }
            if (datosAlumno == null || tipoidentificacion == null || carrera == null) {
                facesMessages.add("Ha ocurrido un error");
                mensaje = "Ocurrió un error procesando su registro, contacte al administrador del sistema para solucionarlo. Puede hacerlo utilizando la opción \"Contactenos\" que se encuentra en el menú y así explicar su caso.";
                if (datosAlumno != null) {
                    mensaje = mensaje + " code: 101.";
                }
                if (tipoidentificacion != null) {
                    mensaje = mensaje + " code: 102.";
                }
                if (carrera != null) {
                    mensaje = mensaje + " code: 103.";
                }
                return "error";
            }
            if (datosAlumno.getFechaNacimiento() != null && datosAlumno.getFechaNacimiento().compareTo(fechaNacimiento) != 0) {
                facesMessages.add("No se han encontrado registros con la informacion suministrada");
                mensaje = "No se han encontrado registros con la informacion suministrada.";
                return "error";
            }
            if (!alumno.getNumSemMatri().equals(carrera.getCarrDuracion())) {
                facesMessages.add("No se pudo realizar el registro");
                mensaje = "Lo sentimos, no podemos realizar el registro debido a que usted aún no se encuentra apto para graduarse.";
                return "error";
            }
            Personageneral pegeSistema = existePersonageneral(alumno.getDocumento(), tipoidentificacion.getTiidId());
            Personanatural penaSistema = null;
            Personauniversidad peruSistema = null;
            if (pegeSistema != null) {
                if (pegeSistema.getPersonanatural() != null) {
                    penaSistema = pegeSistema.getPersonanatural();
                    if (penaSistema.getPersonauniversidad() != null) {
                        peruSistema = penaSistema.getPersonauniversidad();
                        setInstance(pegeSistema.getPersonanatural().getPersonauniversidad());
                        List<Matricula> listaMatriculas = getMatriculas();
                        if (listaMatriculas != null && !listaMatriculas.isEmpty()) {
                            for (Matricula matricula : listaMatriculas) {
                                if (matricula.getMatriCodigo().equals(codigoAlumno)) {
                                    facesMessages.add("Ya existe");
                                    mensaje = "El graduando con el código " + codigoAlumno + " ya se registró en el sistema.";
                                    return "error";
                                }
                            }
                        }
                        opcion = 4;
                    } else {
                        opcion = 3;
                    }
                } else {
                    opcion = 2;
                }
            } else {
                opcion = 1;
            }
            switch(opcion) {
                case 1:
                    pegeSistema = new Personageneral();
                    pegeSistema.setPegeNombre(datosAlumno.getNombres().toUpperCase());
                    pegeSistema.setPegeDocumento(datosAlumno.getDocumento());
                    pegeSistema.setTipoidentificacion(tipoidentificacion);
                    pegeSistema.setPegeEmail(email);
                    pegeSistema.setPegeRegistradopor(registradoPor);
                    pegeSistema.setPegeFechacambio(fechaCambio);
                    entityManager.persist(pegeSistema);
                case 2:
                    penaSistema = new Personanatural();
                    penaSistema.setPenaTiposexo(datosAlumno.getSexo().toUpperCase());
                    penaSistema.setPenaFechanacimiento(datosAlumno.getFechaNacimiento());
                    penaSistema.setPenaNumerolibreta(datosAlumno.getLibretaMilitar());
                    penaSistema.setPenaNumerodistrito(datosAlumno.getDistritoMilitar());
                    penaSistema.setPersonageneral(pegeSistema);
                    penaSistema.setPenaRegistradopor(registradoPor);
                    penaSistema.setPenaFechacambio(fechaCambio);
                    entityManager.persist(penaSistema);
                case 3:
                    peruSistema = new Personauniversidad();
                    peruSistema.setPeruColegiobachiller(alumno.getColegioBachiller());
                    peruSistema.setPeruFechabachiller(alumno.getFecBachiller());
                    peruSistema.setPersonanatural(penaSistema);
                    peruSistema.setPersonanatural(penaSistema);
                    peruSistema.setPeruRegistradopor(registradoPor);
                    peruSistema.setPeruFechacambio(fechaCambio);
                    entityManager.persist(peruSistema);
                case 4:
                    Matricula matricula = new Matricula();
                    matricula.setCarrera(carrera);
                    matricula.setMatriCodigo(codigoAlumno);
                    matricula.setMatriFechaingreso(alumno.getFecIngreso());
                    matricula.setMatriRegistradopor(registradoPor);
                    matricula.setMatriFechacambio(fechaCambio);
                    matricula.setPersonauniversidad(peruSistema);
                    matricula.setMatriRegistradopor(registradoPor);
                    matricula.setMatriFechacambio(fechaCambio);
                    entityManager.persist(matricula);
                    break;
                default:
                    System.out.println("default");
            }
            Usuario usuaSistema = null;
            RolHome rolHome = new RolHome();
            boolean swEmail = false;
            if (opcion != 1) {
                @SuppressWarnings("unchecked") List<Usuario> listaUsuarios = (List<Usuario>) entityManager.createQuery("select usuario from Usuario usuario where usuario.personageneral.pegeId=:pegeId").setParameter("pegeId", pegeSistema.getPegeId()).getResultList();
                if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                    for (Usuario usuario : listaUsuarios) {
                        if (usuario.getRol().getRolNombre().equals("GRADUANDO") || usuario.getRol().getRolNombre().equals("EGRESADO")) {
                            usuario.setRol(rolHome.consultarRol("GRADUANDO"));
                            usuario.setUsuaClave(this.generatePasswordHash(password, usuario.getUsuaNombre()));
                            usuaSistema = usuario;
                            entityManager.merge(usuaSistema);
                            entityManager.flush();
                            swEmail = true;
                            break;
                        }
                    }
                }
            }
            if (usuaSistema == null) {
                usuarioHome.getInstance().setUsuaNombre(nombreUsuario);
                usuarioHome.getInstance().setUsuaClave(password);
                usuarioHome.getInstance().setRol(rolHome.consultarRol("GRADUANDO"));
                usuarioHome.getInstance().setPersonageneral(pegeSistema);
                usuarioHome.persistForm(registradoPor);
                usuaSistema = usuarioHome.getInstance();
            }
            datosEmail.setArchivo("EmailInscripcionGraduando");
            datosEmail.setDireccionRemitente("siegreufps@gmail.com");
            datosEmail.setDireccionDestino(email);
            datosEmail.setNombreDestinatario("Graduando");
            datosEmail.setNombreRemitente("Siegre UFPS");
            datosEmail.setSubject("Registro - Siegre UFPS");
            datosEmail.setSaludo("Hola " + pegeSistema.getPegeNombre());
            if (swEmail) {
                datosEmail.setTexto("Usted ya estaba registrado en el sistema. Hemos actualizado su cuenta para que pueda diligenciar las encuestas respectivas.");
            }
            datosEmail.setNombreUsuario(usuaSistema.getUsuaNombre());
            datosEmail.setClave(password);
            datosEmail.enviarEmail();
        } catch (NoResultException nre) {
            facesMessages.add("Ha ocurrido un error");
            mensaje = "Ocurrió un error procesando su registro, contacte al administrador del sistema para solucionarlo. Puede hacerlo utilizando la opción \"Contactenos\" que se encuentra en el menú y así explicar su caso. code: 104.";
            return "error";
        } catch (InvalidStateException ise) {
            for (InvalidValue invalidValue : ise.getInvalidValues()) {
                log.info("Instance of bean class: " + invalidValue.getBeanClass().getSimpleName() + " has an invalid property: " + invalidValue.getPropertyName() + " with message: " + invalidValue.getMessage());
            }
            facesMessages.add("Ha ocurrido un error");
            mensaje = "Ocurrió un error procesando su registro, contacte al administrador del sistema para solucionarlo. Puede hacerlo utilizando la opción \"Contactenos\" que se encuentra en el menú y así explicar su caso. code: 105.";
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            facesMessages.add("Ha ocurrido un error");
            mensaje = "Ocurrió un error procesando su registro, contacte al administrador del sistema para solucionarlo. Puede hacerlo utilizando la opción \"Contactenos\" que se encuentra en el menú y así explicar su caso. code: 106.";
            return "error";
        }
        return "existe";
    }

    @SuppressWarnings("deprecation")
    private String generatePasswordHash(String password, String salt) {
        AnnotatedBeanProperty<UserPassword> userPasswordProperty = new AnnotatedBeanProperty<UserPassword>(Usuario.class, UserPassword.class);
        String algorithm = userPasswordProperty.getAnnotation().hash();
        return PasswordHash.instance().generateSaltedHash(password, salt, algorithm);
    }

    public String getSelectedTab() {
        selectedTab = BordeBean.seleccionarBorde();
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public void setListaMatricula(List<Matricula> listaMatricula) {
        this.listaMatricula = listaMatricula;
    }

    public List<Matricula> getListaMatricula() {
        if (swLista) {
            listaMatricula.addAll(getMatriculas());
            swLista = false;
        }
        return listaMatricula;
    }

    public String agregarMatricula() {
        listaMatricula.add(matriculaHome.getInstance());
        matriculaHome.clearInstance();
        return "agregado";
    }

    public String borrarMatricula() {
        listaMatricula.remove(matriculaHome.getInstance());
        matriculaHome.clearInstance();
        return "borrado";
    }

    public String editarMatricula() {
        matriculaHome.clearInstance();
        return "editado";
    }

    public void setIndexLista(int indexLista) {
        this.indexLista = indexLista;
        if (this.indexLista > -1) matriculaHome.setInstance(listaMatricula.get(this.indexLista)); else matriculaHome.clearInstance();
    }

    public int getIndexLista() {
        return indexLista;
    }

    public void insertarMatricula(List<Matricula> lista) {
        for (Matricula matri : lista) {
            matri.preparar(getInstance());
            entityManager.persist(matri);
        }
        entityManager.flush();
    }
}

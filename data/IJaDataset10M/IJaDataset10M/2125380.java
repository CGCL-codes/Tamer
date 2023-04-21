package com.siasal.usuarios.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.common.business.ServiceObject;
import com.common.exception.BusinessException;
import com.common.exception.InternalApplicationException;
import com.common.exception.ValidationException;
import com.common.log.ILogger;
import com.common.log.LogFactory;
import com.common.persistence.domainstore.IPersistenceManager;
import com.common.persistence.domainstore.PersistenceManagerFactory;
import com.common.to.ServiceResponseTO;
import com.common.transaction.TransactionContext;
import com.siasal.bibliotecarios.business.Administrador;
import com.siasal.bibliotecarios.business.LaborDiaria;
import com.siasal.documentos.business.DocumentoBasico;
import com.siasal.documentos.business.Ejemplar;
import com.siasal.documentos.commons.DatosEjemplarTO;
import com.siasal.pedidos.commons.EstadoUsuarioTO;
import com.siasal.prestamos.business.Prestamo;
import com.siasal.prestamos.business.Reservacion;
import com.siasal.prestamos.commons.AtrasoItemTO;
import com.siasal.prestamos.commons.DatosPrestamoTO;
import com.siasal.prestamos.commons.DevolucionItemTO;
import com.siasal.usuarios.business.Donacion;
import com.siasal.usuarios.business.Multa;
import com.siasal.usuarios.business.Persona;
import com.siasal.usuarios.business.TipoUC;
import com.siasal.usuarios.business.UsuarioConsulta;
import com.siasal.usuarios.commons.PersonaTO;
import com.siasal.usuarios.commons.UsuarioPrestamoItemTO;

/**
 * 
 * @modelguid {3623063C-C7C5-4686-BD1E-EB2F0F6572AF}
 */
public class UsuariosService extends ServiceObject {

    String getServiceName() {
        return "UsuariosService";
    }

    public UsuariosService(TransactionContext transactionContext) {
        super(transactionContext);
    }

    public UsuariosService() {
    }

    /**
	 * 
	 * @modelguid {AE223D4D-19C9-44E0-9F8A-956126EF50AB}
	 */
    public Integer registrarDonacionDinero(Donacion donacion, String loginBibliotecario) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        if (logger.isDebugEnabled()) {
            logger.logDebug("Ingresa al metodo registrarDonacionDinero parametros: donacion=" + donacion + " bibliotecario=" + loginBibliotecario);
        }
        try {
            if (donacion == null || loginBibliotecario == null) {
                throw new ValidationException("No se puede registrar donacion de ejemplar  alguno de los argumentos son ==> null, cero");
            }
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            Map parametros = new HashMap();
            parametros.put("login", loginBibliotecario);
            Administrador administradorTmp = (Administrador) pm.getUniqueNamedQuery("buscarAdministradorPorLogin", parametros);
            if (administradorTmp == null) {
                throw new BusinessException("No se puedo encontrar administrador con ese login");
            }
            donacion.setAdministrador(administradorTmp);
            pm.save(donacion);
            transactionContext.commitAll();
            return donacion.getId();
        } catch (BusinessException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar ==>" + donacion, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {21B09FEE-C4FA-42FF-A5D4-9F83DD81D80D}
	 */
    public Integer registrarDonacionEjemplar(Ejemplar ejemplar, Donacion donacion, String loginBibliotecario) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        if (logger.isDebugEnabled()) {
            logger.logDebug("Ingresa al metodo registrarDonacionEjemplar parametros: ejemplar=" + ejemplar + " donacion=" + donacion + " Administrador=" + loginBibliotecario);
        }
        try {
            if (ejemplar == null || donacion == null || loginBibliotecario == null) {
                throw new ValidationException("No se puede registrar donacion de ejemplar  alguno de los argumentos son ==> null");
            }
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            Map parametros = new HashMap();
            parametros.put("codigo", ejemplar.getCodigo());
            Ejemplar ejmplarTmp = (Ejemplar) pm.getUniqueNamedQuery("getByCodigo", parametros);
            if (ejmplarTmp == null) {
                throw new BusinessException("No se encuentra Ejemplar con c�digo:" + ejemplar.getCodigo());
            }
            donacion.setEjemplar(ejmplarTmp);
            parametros = new HashMap();
            parametros.put("login", loginBibliotecario);
            Administrador administradorTmp = (Administrador) pm.getUniqueNamedQuery("buscarAdministradorPorLogin", parametros);
            if (administradorTmp == null) {
                throw new BusinessException("No se puedo encontrar administrador con ese login");
            }
            donacion.setAdministrador(administradorTmp);
            pm.save(donacion);
            transactionContext.commitAll();
            return donacion.getId();
        } catch (BusinessException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar ==>" + ejemplar, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {290238FC-DDCE-46C1-94CF-50A19B202C99}
	 */
    public void crearSuscripcion() {
    }

    /**
	 * 
	 * @modelguid {5FB401CF-34AC-4360-A35F-99FA3C7F5319}
	 */
    public void renovarSuscripcion(UsuarioConsulta usuarioConsulta) throws BusinessException {
        if (usuarioConsulta == null) {
            throw new ValidationException("No se puede crear objeto Usuario de Consulta ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            usuarioConsulta.renovarSuscripcion();
            pm.saveOrUpdate(usuarioConsulta);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (BusinessException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al renovar ==>" + usuarioConsulta, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    public void reservarEjemplar(String cedulaUsuario, String codigoEjemplar) throws BusinessException {
        if (cedulaUsuario == null) {
            throw new ValidationException("No se puede crear objeto Usuario de Consulta ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            UsuarioConsulta usuarioConsultaTmp = buscarUsuarioConsulta(cedulaUsuario);
            if (!usuarioConsultaTmp.getTipo().getReservar()) {
                throw new BusinessException("Usuario no puede reservar ejemplares");
            }
            Reservacion reservacionTmp = new Reservacion();
            reservacionTmp.setEstado(Reservacion.PENDIENTE);
            reservacionTmp.setFecha(new Date());
            reservacionTmp.setUsuario(usuarioConsultaTmp);
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("codigo", codigoEjemplar);
            Ejemplar ejemplar = (Ejemplar) pm.getUniqueNamedQuery("getByCodigo", parametros);
            if (ejemplar == null) {
                throw new BusinessException("No existe ejemplar con el c�digo:" + codigoEjemplar);
            }
            parametros = new HashMap<String, String>();
            parametros.put("codigoEjemplar", codigoEjemplar);
            Reservacion reservacion = (Reservacion) pm.getUniqueNamedQuery("getReservacion", parametros);
            ejemplar.setEstado(Ejemplar.RESERVADO);
            if (reservacion != null) {
                throw new BusinessException("Ejemplar ya reservado");
            }
            reservacionTmp.setEjemplar(ejemplar);
            pm.save(reservacionTmp);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (BusinessException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al renovar ==>" + cedulaUsuario, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {AE9605F9-0A74-497D-AEB0-AAB4CAC6AB3B}
	 */
    public void crearTipoUC(TipoUC tipoUC) throws BusinessException {
        if (tipoUC == null) {
            throw new ValidationException("No se puede crear objeto tipoUC ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            tipoUC.setEstado(OBJECT_AVAILABLE);
            pm.save(tipoUC);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar ==>" + tipoUC, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    public List<TipoUC> consultarTiposUC() {
        try {
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            List listTiposUC = (List) pm.find("from TipoUC c where c.estado = 0");
            return listTiposUC;
        } catch (InternalApplicationException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalApplicationException("Error al consultar Tipos de Usuarios de Consulta", e);
        }
    }

    /**
	 * 
	 * @modelguid {AE9605F9-0A74-497D-AEB0-AAB4CAC6AB3B}
	 */
    public void actualizarTipoUC(TipoUC tipoUC) throws BusinessException {
        if (tipoUC == null) {
            throw new ValidationException("No se puede crear objeto tipoUC ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            pm.update(tipoUC);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar ==>" + tipoUC, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {AE9605F9-0A74-497D-AEB0-AAB4CAC6AB3B}
	 */
    public void eliminarTipoUC(TipoUC tipoUC) throws BusinessException {
        if (tipoUC == null) {
            throw new ValidationException("No se puede crear objeto tipoUC ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            tipoUC.setEstado(OBJECT_DELETED);
            pm.update(tipoUC);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar ==>" + tipoUC, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {4FC5175C-3D83-4038-A207-744303825F7B}
	 */
    public void eliminarTipoUC() {
    }

    /**
	 * 
	 * @modelguid {DE821680-D694-4D27-B39F-202FB0D2C9DD}
	 */
    public void pagarMulta() {
    }

    /**
	 * 
	 * @modelguid {7E4AEC57-7D4F-49C4-AED4-FBED2DC482AF}
	 */
    public void actualizarMulta(Multa multa) throws BusinessException {
        if (multa == null) {
            throw new ValidationException("No se puede crear objeto multa ==> null");
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            float saldo = 0f;
            float montoOrg = 0f;
            float pagado = 0f;
            Multa multaTmp = (Multa) pm.get(Multa.class, multa.getId());
            saldo = multaTmp.getSaldo();
            montoOrg = multaTmp.getMonto();
            pagado = montoOrg - saldo;
            multaTmp.setSaldo(multa.getMonto());
            multaTmp.setMonto(multa.getMonto());
            multaTmp.pagar(pagado);
            pm.update(multaTmp);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al actualizar ==>" + multa, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {CECB5F8E-DE1F-40DA-8660-8E1C95119E98}
	 */
    public void crearUC(UsuarioConsulta usuarioConsulta) throws BusinessException {
        if (usuarioConsulta == null) {
            throw new ValidationException("No se puede crear objeto usuarioConsulta ==> null");
        }
        ILogger logger = LogFactory.getLogger(this);
        if (logger.isDebugEnabled()) {
            logger.logDebug("Ingresa al metodo crearUC parametros: usuario=" + usuarioConsulta);
        }
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            usuarioConsulta.setEstadoUC(OBJECT_AVAILABLE);
            usuarioConsulta.crearSuscripcion();
            pm.save(usuarioConsulta);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al guardar usuario de consulta==>", e);
        } finally {
            this.transactionContext.closeAll();
        }
    }

    /**
	 * 
	 * @modelguid {2DB88606-010A-4510-830E-D5774CF2B886}
	 */
    public void actualizarUC() {
    }

    /**
	 * 
	 * @modelguid {491B9867-3D89-4F51-B079-07629C323E90}
	 */
    public void imprimirCarneUC() {
    }

    /**
	 * 
	 * @modelguid {D4BECF50-39C7-4BB3-B570-76C0DA7F805D}
	 */
    public void imprimirCertificadoUC() {
    }

    /**
	 * 
	 * @modelguid {A5A6773D-619B-4B6B-9045-B32D7585FE3F}
	 */
    public void imprimirArchivoUC() {
    }

    /**
	 * 
	 * @modelguid {BD21B06B-C2FA-44AD-8EE2-E631CDA0CAA2}
	 */
    public void puedeLlevarUC() {
    }

    /**
	 * 
	 * @modelguid {5098B457-9FA4-4FA6-9DF1-B9495C5BA45C}
	 */
    public void prestamosUC() {
    }

    /**
	 * 
	 * @modelguid {12618123-BF5B-4CAC-969C-DE44D9A5B493}
	 */
    public void habilitadoUC() {
    }

    /**
	 * 
	 * @modelguid {DAAD7E15-0C34-4B5B-8470-994A42D04995}
	 */
    public void eliminarUC() {
    }

    /**
	 * 
	 * @modelguid {68906421-9645-4B44-AD23-1BFEF2761E38}
	 */
    public void listarUCAtrasadosDevEjem() {
    }

    /**
	 * 
	 * @modelguid {BD8E3E56-32E4-461D-8074-D08DEBE3FC0A}
	 */
    public void listarUCMultados() {
    }

    /**
	 * 
	 * @modelguid {05EA3970-B0E4-44F1-805C-E4994F9456E8}
	 */
    public ServiceResponseTO listarUC() {
        try {
            transactionContext.begin();
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            List listTiposUC = (List) pm.find("from TipoUC a where a.estado = 0 or a.estado = 1 order by a.nombre");
            Iterator iterator = listTiposUC.iterator();
            List contents = new ArrayList();
            while (iterator.hasNext()) {
                TipoUC tipoUC = (TipoUC) iterator.next();
                contents.add(tipoUC.getTO());
            }
            transactionContext.commitAll();
            return new ServiceResponseTO(contents);
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al consultar los tipos de usuarios de consulta.", e);
        }
    }

    /** ********** ESTOS METODOS NO SON DEL NEGOCIO**************** */
    public void crearPersona(PersonaTO personaTo) {
        transactionContext.begin();
        Persona persona = new Persona(personaTo);
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        pm.save(persona);
        transactionContext.commitAll();
    }

    public UsuarioConsulta buscarUsuarioConsulta(String cedula) throws BusinessException {
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        Map<String, String> parametros = new Hashtable<String, String>();
        parametros.put("cedula", cedula);
        UsuarioConsulta usuarioConsulta = (UsuarioConsulta) pm.getUniqueNamedQuery("getUsuarioConsultaByCedula", parametros);
        if (usuarioConsulta == null) {
            throw new BusinessException("No se encuentra usuario con esa cedula:" + cedula);
        }
        usuarioConsulta.getTipo().getNombre();
        return usuarioConsulta;
    }

    public List buscarMultasMora(TipoUC tipoUC) throws BusinessException {
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        Map<String, Integer> parametros = new Hashtable<String, Integer>();
        parametros.put("idTipo", tipoUC.getId());
        List multasMora = pm.getNamedQuery("Multas.getMultasMoraByTipo", parametros);
        if (multasMora.isEmpty()) {
            throw new BusinessException("No se encuentra multas con este tipo");
        }
        Iterator it = multasMora.iterator();
        while (it.hasNext()) {
            Multa multaMora = (Multa) it.next();
            multaMora.getUsuario().getApellido();
        }
        return multasMora;
    }

    public List buscarUsuarioConsulta(TipoUC tipoUC) throws BusinessException {
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        Map<String, Integer> parametros = new Hashtable<String, Integer>();
        parametros.put("idTipo", tipoUC.getId());
        List usuariosConsulta = pm.getNamedQuery("getUsuariosConsultaByTipo", parametros);
        if (usuariosConsulta.isEmpty()) {
            throw new BusinessException("No se encuentra usuarios de ese tipo");
        }
        return usuariosConsulta;
    }

    public UsuarioPrestamoItemTO buscarUsuarioConsultaPrestamo(String cedula) throws BusinessException {
        UsuarioConsulta usuarioConsulta = (UsuarioConsulta) buscarUsuarioConsulta(cedula);
        UsuarioPrestamoItemTO usuarioPrestamo = new UsuarioPrestamoItemTO();
        usuarioPrestamo.setId(usuarioConsulta.getId());
        usuarioPrestamo.setCedula(usuarioConsulta.getCedula());
        usuarioPrestamo.setUsuario(usuarioConsulta.getNombre() + " " + usuarioConsulta.getApellido());
        return usuarioPrestamo;
    }

    /**
	 * Funcion que permite obtener el estado del usuario
	 * 
	 * @param codigoUsuario
	 *            cedula del usuario
	 * @return
	 * @throws BusinessException
	 */
    public EstadoUsuarioTO revisarEstadoUsuario(String codigoUsuario) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        String methodId = MessageFormat.format(ILogger.METHOD_FORMAT, getServiceName(), "revisarEstadoUsuario");
        List ejemplaresPrestadosTmp = new ArrayList();
        List reservacionesTmp = new ArrayList();
        List multasTmp = new ArrayList();
        if (logger.isDebugEnabled()) {
            logger.logDebug(methodId + "parametro c�digo:" + codigoUsuario);
        }
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        Map<String, String> parametros = new Hashtable<String, String>();
        parametros.put("cedula", codigoUsuario);
        UsuarioConsulta usuarioConsulta = (UsuarioConsulta) pm.getUniqueNamedQuery("getUsuarioConsultaByCedula", parametros);
        if (usuarioConsulta == null) {
            throw new BusinessException("No se encuentra usuario con esa cedula:" + codigoUsuario);
        }
        Iterator itMultas = usuarioConsulta.getMultas().iterator();
        Multa multaTmp = null;
        while (itMultas.hasNext()) {
            multaTmp = (Multa) itMultas.next();
            if (multaTmp.getEstado() == Multa.PENDIENTE) {
                multasTmp.add(multaTmp);
            }
        }
        Map<String, Integer> parametrosPrestamos = new HashMap<String, Integer>();
        parametrosPrestamos.put("usuarioId", usuarioConsulta.getId());
        List<Prestamo> prestamoResultListTmp = (List<Prestamo>) pm.getNamedQuery("getPrestamosPorUsuario", parametrosPrestamos);
        Iterator<Prestamo> itResultListTmp = prestamoResultListTmp.iterator();
        while (itResultListTmp.hasNext()) {
            Prestamo prestamoTmp = itResultListTmp.next();
            DocumentoBasico documentoBasico = (DocumentoBasico) pm.get(DocumentoBasico.class, prestamoTmp.getEjemplar().getDocumento().getId());
            DevolucionItemTO devolucionItemTO = new DevolucionItemTO();
            devolucionItemTO.setIdEjemplar(prestamoTmp.getEjemplar().getId());
            devolucionItemTO.setCodigo(prestamoTmp.getEjemplar().getCodigo());
            devolucionItemTO.setIdPrestamo(prestamoTmp.getId());
            devolucionItemTO.setNombre(documentoBasico.getTitulo());
            devolucionItemTO.setAutores(documentoBasico.getAutoresConcatenados());
            devolucionItemTO.setNumeroDias(prestamoTmp.calcularDiasRetraso());
            devolucionItemTO.setIdUsuario(prestamoTmp.getUsuario().getId());
            ejemplaresPrestadosTmp.add(devolucionItemTO);
        }
        Map<String, Integer> parametrosReservaciones = new HashMap<String, Integer>();
        parametrosReservaciones.put("usuarioId", usuarioConsulta.getId());
        List<Reservacion> reservacionResultListTmp = (List<Reservacion>) pm.getNamedQuery("getReservacionesPorUsuario", parametrosReservaciones);
        Iterator<Reservacion> itReservacionResultListTmp = reservacionResultListTmp.iterator();
        while (itReservacionResultListTmp.hasNext()) {
            Reservacion reservacionTmp = itReservacionResultListTmp.next();
            DocumentoBasico documentoBasico = (DocumentoBasico) pm.get(DocumentoBasico.class, reservacionTmp.getEjemplar().getDocumento().getId());
            DevolucionItemTO devolucionItemTO = new DevolucionItemTO();
            devolucionItemTO.setIdEjemplar(reservacionTmp.getEjemplar().getId());
            devolucionItemTO.setCodigo(reservacionTmp.getEjemplar().getCodigo());
            devolucionItemTO.setIdPrestamo(reservacionTmp.getId());
            devolucionItemTO.setNombre(documentoBasico.getTitulo());
            devolucionItemTO.setAutores(documentoBasico.getAutoresConcatenados());
            devolucionItemTO.setIdUsuario(reservacionTmp.getUsuario().getId());
            reservacionesTmp.add(devolucionItemTO);
        }
        EstadoUsuarioTO estadoUsuarioTO = new EstadoUsuarioTO();
        estadoUsuarioTO.setEjemplaresPrestados(ejemplaresPrestadosTmp);
        estadoUsuarioTO.setMultas(multasTmp);
        estadoUsuarioTO.setReservaciones(reservacionesTmp);
        return estadoUsuarioTO;
    }

    /**
	 * Metodo que nos devuelve las doonaciones existentes de acuerdo al usuario 
	 * 
	 * 
	 * @param usuario
	 * @return
	 * @throws BusinessException
	 */
    public List buscarDonaciones(UsuarioConsulta usuario) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        String methodId = MessageFormat.format(ILogger.METHOD_FORMAT, getServiceName(), "buscarDonaciones");
        try {
            IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
            Map parametros = new HashMap();
            parametros.put("codigoUsuario", usuario.getId());
            List listDonaciones = pm.getNamedQuery("DonacionUsuarios.getbyUsuario", parametros);
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodId + "donaciones encontradas:" + listDonaciones.size());
            }
            Iterator it = listDonaciones.iterator();
            while (it.hasNext()) {
                Donacion tmpDonacion = (Donacion) it.next();
                if (tmpDonacion.getEjemplar() != null) {
                    DocumentoBasico documentoBasicoTmp = (DocumentoBasico) pm.get(DocumentoBasico.class, tmpDonacion.getEjemplar().getDocumento().getId());
                    documentoBasicoTmp.getTitulo();
                    tmpDonacion.getEjemplar().setDocumento(documentoBasicoTmp);
                }
                tmpDonacion.getMonto();
                tmpDonacion.getTipoLabel();
            }
            return listDonaciones;
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            this.transactionContext.rollbackAll();
            throw new InternalApplicationException("Error de Infraestructura al Consultar Donaciones", e);
        }
    }

    /**
	 * Funcion que permite obtener la lista de ejemplares que tienen atraso en la devolucion
	 * 
	 * @param tipoUC
	 *            id del Tipo de Usuario de Consulta
	 * @return
	 * @throws BusinessException
	 */
    public List buscarAtrasosDevoluciones(Integer tipoUC) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        String methodId = MessageFormat.format(ILogger.METHOD_FORMAT, getServiceName(), "buscarAtrasosDevoluciones");
        if (tipoUC == null) {
            throw new ValidationException("No se puede buscar atrasos tipoUC ==> null");
        }
        List ejemplaresPrestadosTmp = new ArrayList();
        if (logger.isDebugEnabled()) {
            logger.logDebug(methodId + "parametro tipoUC:" + tipoUC);
        }
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        Map<String, Integer> parametros = new Hashtable<String, Integer>();
        parametros.put("idTipo", tipoUC);
        List usuariosConsulta = pm.getNamedQuery("getUsuariosConsultaByTipo", parametros);
        if (usuariosConsulta.isEmpty()) {
            throw new BusinessException("No se encuentra usuarios de ese tipo");
        }
        Iterator<UsuarioConsulta> it = usuariosConsulta.iterator();
        while (it.hasNext()) {
            UsuarioConsulta usuarioConsulta = it.next();
            Map<String, Integer> parametrosPrestamos = new HashMap<String, Integer>();
            parametrosPrestamos.put("usuarioId", usuarioConsulta.getId());
            List<Prestamo> prestamoResultListTmp = (List<Prestamo>) pm.getNamedQuery("getPrestamosPorUsuario", parametrosPrestamos);
            Iterator<Prestamo> itResultListTmp = prestamoResultListTmp.iterator();
            while (itResultListTmp.hasNext()) {
                Prestamo prestamoTmp = itResultListTmp.next();
                DocumentoBasico documentoBasico = (DocumentoBasico) pm.get(DocumentoBasico.class, prestamoTmp.getEjemplar().getDocumento().getId());
                AtrasoItemTO atrasoItemTO = new AtrasoItemTO();
                atrasoItemTO.setIdEjemplar(prestamoTmp.getEjemplar().getId());
                atrasoItemTO.setCodigo(prestamoTmp.getEjemplar().getCodigo());
                atrasoItemTO.setIdPrestamo(prestamoTmp.getId());
                atrasoItemTO.setNombre(documentoBasico.getTitulo());
                atrasoItemTO.setAutores(documentoBasico.getAutoresConcatenados());
                atrasoItemTO.setNumeroDias(prestamoTmp.calcularDiasRetraso());
                atrasoItemTO.setUsuario(prestamoTmp.getUsuario());
                ejemplaresPrestadosTmp.add(atrasoItemTO);
            }
        }
        return ejemplaresPrestadosTmp;
    }

    /**
	 * Funcion que permite obtener la lista de prestamos pendientes
	 * 
	 * @param 
	 * @return List
	 * @throws BusinessException
	 */
    public List buscarPrestamosPendientes() throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        String methodId = MessageFormat.format(ILogger.METHOD_FORMAT, getServiceName(), "buscarPrestamosPendientes");
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        List prestamosPendientes = new ArrayList();
        prestamosPendientes = pm.getNamedQuery("Prestamo.getPrestamosPendientes");
        if (prestamosPendientes.isEmpty()) {
            throw new BusinessException("No se encuentran pr�stamos pendientes");
        }
        List<DatosPrestamoTO> prestamosPendientesTO = new ArrayList<DatosPrestamoTO>();
        Iterator<Prestamo> it = prestamosPendientes.iterator();
        while (it.hasNext()) {
            DatosPrestamoTO prestamoTO = new DatosPrestamoTO();
            Prestamo prestamo = it.next();
            prestamo.getEjemplar().getCodigo();
            DocumentoBasico documentoBasico = (DocumentoBasico) pm.get(DocumentoBasico.class, prestamo.getEjemplar().getDocumento().getId());
            prestamoTO.setTitulo(documentoBasico.getTitulo());
            prestamoTO.setNombreCompleto(prestamo.getUsuario().getNombreCompleto());
            prestamo.getEstadoLabel();
            prestamoTO.setCaducidad(prestamo.getCaducidad());
            prestamoTO.setDevolucion(prestamo.getDevolucion());
            prestamoTO.setDias(prestamo.getDias());
            prestamoTO.setEjemplar(prestamo.getEjemplar());
            prestamoTO.setEstado(prestamo.getEstado());
            prestamoTO.setFecha(prestamo.getFecha());
            prestamoTO.setId(prestamo.getId());
            prestamoTO.setMulta(prestamo.getMulta());
            prestamoTO.setRenovado(prestamo.getRenovado());
            prestamoTO.setUsuario(prestamo.getUsuario());
            prestamo.getUsuario().getNombreCompleto();
            prestamoTO.setDocumento(documentoBasico);
            documentoBasico.getTitulo();
            prestamoTO.setCodigo(prestamo.getEjemplar().getCodigo());
            prestamosPendientesTO.add(prestamoTO);
        }
        return prestamosPendientesTO;
    }

    public void actualizarDatosPrestamo(DatosPrestamoTO prestamo) throws BusinessException {
        ILogger logger = LogFactory.getLogger(this);
        String methodId = MessageFormat.format(ILogger.METHOD_FORMAT, getServiceName(), "actualizarDatosPrestamo");
        IPersistenceManager pm = PersistenceManagerFactory.getPersistenceManager();
        try {
            transactionContext.begin();
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("idPrestamo", prestamo.getCodigo());
            Prestamo prestamoTmp = (Prestamo) pm.getUniqueNamedQuery("getPrestamoPorId", parametros);
            if (prestamoTmp == null) {
                throw new BusinessException("No existe prestamo con el codigo:" + prestamo.getId());
            }
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodId + "actualizando prestamo:" + prestamo.getId());
            }
            prestamoTmp.setEstado(prestamo.getEstado());
            pm.update(prestamoTmp);
            transactionContext.commitAll();
        } catch (InternalApplicationException e) {
            this.transactionContext.rollbackAll();
            throw e;
        } catch (Throwable e) {
            transactionContext.rollbackAll();
            throw new InternalApplicationException("Error al actualizar ==>" + prestamo, e);
        } finally {
            this.transactionContext.closeAll();
        }
    }
}

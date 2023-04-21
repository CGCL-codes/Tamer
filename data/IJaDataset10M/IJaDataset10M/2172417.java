package pe.com.bn.sach.service;

import java.util.List;
import pe.com.bn.sach.domain.Bnchf01Persona;
import pe.com.bn.sach.domain.Bnchf02Cliente;
import pe.com.bn.sach.domain.Bnchf13Expediente;
import pe.com.bn.sach.domain.BnhbReniec;
import pe.com.bn.sach.domain.Bnchf12Familiar;
import pe.com.bn.sach.domain.Bnchf67DireccionPers;

public interface ClienteService {

    public String getcountEstadoBandeja(Bnchf13Expediente bnchf13Expediente) throws Exception;

    public int ExistePersona(Bnchf01Persona Bnchf01Person) throws Exception;

    public String getCountExpedienteEnTramitePersona(Long idPersona) throws Exception;

    public String getCountExpedienteEnTramite(Long idCliente) throws Exception;

    public List getExpedienteGeneradoPrecalificado(Long idCliente) throws Exception;

    public boolean SoloPuedeModificarClienteEnEstaCondicion(List rolesList, Bnchf13Expediente bnchf13Expediente);

    public boolean getVerificaGeneradoPrecalificado(Long idCliente) throws Exception;

    public int ModificarEstadoCivilCliente(Bnchf02Cliente Bnchf02Cliente, Bnchf01Persona Bnchf01Persona, Bnchf67DireccionPers Bnchf67DireccionPers) throws Exception;

    public List listFamiliar(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public void forinsert(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public void forinsertcommit(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public int clientesoltero(Bnchf02Cliente bnchf02Cliente) throws Exception;

    public int personasoltera(Long idCliente) throws Exception;

    public int personasoltera(Bnchf01Persona bnchf01Persona) throws Exception;

    public void ActualizaEstadoParentesco(Bnchf12Familiar bnchf12Familiar) throws Exception;

    public List listClientesinParam() throws Exception;

    public Long getEstadoParentesco(Bnchf12Familiar bnchf12Familiar) throws Exception;

    public List listCliente(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public Bnchf02Cliente guardaCliente(Bnchf02Cliente Bnchf02Cliente, Bnchf01Persona Bnchf01Persona, Bnchf67DireccionPers Bnchf67DireccionPers) throws Exception;

    public void guardaFamiliar(Bnchf02Cliente Bnchf02Cliente, Bnchf01Persona Bnchf01Persona, Bnchf12Familiar Bnchf12Familiar, Bnchf67DireccionPers Bnchf67DireccionPers) throws Exception;

    public List listFamilia(Bnchf12Familiar Bnchf12Familiar) throws Exception;

    public Bnchf02Cliente getCliente(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public Bnchf01Persona getPersona(Bnchf01Persona Bnchf01Persona) throws Exception;

    public int preguntacliente(Bnchf12Familiar Bnchf12Familiar) throws Exception;

    public void actualizaCliente(Bnchf02Cliente Bnchf02Cliente, Bnchf01Persona Bnchf01Persona, Bnchf67DireccionPers Bnchf67DireccionPers) throws Exception;

    public void ActualizarFamiliar(Bnchf02Cliente bnchf02Cliente, Bnchf01Persona bnchf01Persona, Bnchf67DireccionPers Bnchf67DireccionPers) throws Exception;

    public List DireccionPesona(Bnchf67DireccionPers bnchf84DireccionPers) throws Exception;

    public int ActualizarEstado(Bnchf02Cliente bnchf02Cliente) throws Exception;

    public int ExisteCliente(Bnchf02Cliente bnchf02Cliente) throws Exception;

    public int ActualizaEstadoPersona(Bnchf01Persona bnchf01Persona) throws Exception;

    public int stadoexpedientesEnTramite(Bnchf01Persona Bnchf01Persona) throws Exception;

    public int stadoexpedientesConIngresofam(Bnchf01Persona Bnchf01Persona) throws Exception;

    public List SearchPopup(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public Bnchf02Cliente FindPopup(Bnchf02Cliente Bnchf02Cliente) throws Exception;

    public BnhbReniec buscarFoto(String dni) throws Exception;

    public BnhbReniec buscarFirma(String dni) throws Exception;
}

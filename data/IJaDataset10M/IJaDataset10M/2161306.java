package org.digitall.apps.taxes092.classes;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.digitall.apps.taxes.classes.TipoImpuesto;
import org.digitall.apps.taxes.classes.TipoPlanDePago;
import org.digitall.lib.data.Format;
import org.digitall.lib.sql.LibSQL;

public class ConfiguracionPlanDePago {

    private int menorAnio = 0;

    private int mayorAnio = 0;

    private int menorMes = 0;

    private int mayorMes = 0;

    private int menorAnticipo = 0;

    private int mayorAnticipo = 0;

    private int cantidadAnticipos = 0;

    private double capitalPuro = 0.0;

    private double interesPuro = 0.0;

    private double totalAnticiposAdeudadosPuro = 0.0;

    private int menorAnioxContado = 0;

    private int mayorAnioxContado = 0;

    private int menorMesxContado = 0;

    private int mayorMesxContado = 0;

    private int menorAnticipoxContado = 0;

    private int mayorAnticipoxContado = 0;

    private int cantidadAnticiposxContado = 0;

    private double capitalPuroxContado = 0.0;

    private double interesPuroxContado = 0.0;

    private double totalAnticiposAdeudadosPuroxContado = 0.0;

    private double totalPagoxContado = 0.0;

    private double porcentajeCondonacionIntereses = 0.0;

    private double montoCondonacionIntereses = 0.0;

    private double porcentajeBonificacion = 0.0;

    private double montoBonificacion = 0.0;

    private double montoBonificacionCtdo = 0.0;

    private double capital = 0.0;

    private double interes = 0.0;

    private double totalAnticiposAdeudados = 0.0;

    private double totalAFinanciar = 0.0;

    private int cantidadCuotas = 0;

    private int diaVencimiento = 0;

    private double porcentajeInteresFinanciacion = 0.0;

    private double montoInteresFinanciacion = 0.0;

    private double valorPrimerCuota = 0.0;

    private double valorCuotasRestantes = 0.0;

    private double montoTotalCuotas = 0.0;

    private double total = 0.0;

    private double valorAnticipoActual = 0.0;

    private double valorDescuentoAnticipoActual = 0.0;

    private double valorNetoAnticipoActual = 0.0;

    private double totalAPagar = 0.0;

    private boolean pagoContado = false;

    private double porcentajeDescuentoPagoContado = 0.0;

    private double montoDescuentoPagoContado = 0.0;

    private double subTotalEstadoCuenta = 0.0;

    private int cantAnticiposCubiertosEntrega = 0;

    private int menorAnioEntrega = 0;

    private int menorAnticipoEntrega = 0;

    private int mayorAnioEntrega = 0;

    private int mayorAnticipoEntrega = 0;

    private int anticipoActualEntrega = 0;

    private int anioAnticipoActualEntrega = 0;

    private int menorAnioPPEntrega = 0;

    private int menorAnticipoPPEntrega = 0;

    private int mayorAnioPPEntrega = 0;

    private int mayorAnticipoPPEntrega = 0;

    private double montoBaseCubiertoEntrega = 0.0;

    private double interesBaseCubiertoEntrega = 0.0;

    private double totalBaseCubiertoEntrega = 0.0;

    private double montoBaseCubiertoEntregaSinDto = 0.0;

    private double interesBaseCubiertoEntregaSinDto = 0.0;

    private double totalBaseCubiertoEntregaSinDto = 0.0;

    private double totalAnticiposAdeudadosEntrega = 0.0;

    private double saldoEntrega = 0.0;

    private double pagoAnticipado = 0.0;

    private double montoBasePPEntrega = 0.0;

    private double interesBasePPEntrega = 0.0;

    private double totalBasePPEntrega = 0.0;

    private double montoBasePPEntregaSinDto = 0.0;

    private double interesBasePPEntregaSinDto = 0.0;

    private double totalBasePPEntregaSinDto = 0.0;

    private double totalAPAgarSinEntrega = 0.0;

    private double totalAPagarConEntrega = 0.0;

    private double totalAPagarCuotas = 0.0;

    private Bonificacion bonificacion;

    private boolean generado = false;

    public ConfiguracionPlanDePago() {
    }

    public void setMenorAnio(int menorAnio) {
        this.menorAnio = menorAnio;
    }

    public int getMenorAnio() {
        return menorAnio;
    }

    public void setMayorAnio(int mayorAnio) {
        this.mayorAnio = mayorAnio;
    }

    public int getMayorAnio() {
        return mayorAnio;
    }

    public void setMenorMes(int menorMes) {
        this.menorMes = menorMes;
    }

    public int getMenorMes() {
        return menorMes;
    }

    public void setMayorMes(int mayorMes) {
        this.mayorMes = mayorMes;
    }

    public int getMayorMes() {
        return mayorMes;
    }

    public void setmenorAnticipo(int menorAnticipo) {
        this.menorAnticipo = menorAnticipo;
    }

    public int getmenorAnticipo() {
        return menorAnticipo;
    }

    public void setmayorAnticipo(int mayorAnticipo) {
        this.mayorAnticipo = mayorAnticipo;
    }

    public int getmayorAnticipo() {
        return mayorAnticipo;
    }

    public void setCantidadAnticipos(int cantidadAnticipos) {
        this.cantidadAnticipos = cantidadAnticipos;
    }

    public int getCantidadAnticipos() {
        return cantidadAnticipos;
    }

    public void setPorcentajeCondonacionIntereses(double porcentajeCondonacionIntereses) {
        this.porcentajeCondonacionIntereses = porcentajeCondonacionIntereses;
    }

    public double getPorcentajeCondonacionIntereses() {
        return porcentajeCondonacionIntereses;
    }

    public void setMontoCondonacionIntereses(double montoCondonacionIntereses) {
        this.montoCondonacionIntereses = montoCondonacionIntereses;
    }

    public double getMontoCondonacionIntereses() {
        return montoCondonacionIntereses;
    }

    public void setTotalAnticiposAdeudados(double totalAnticiposAdeudados) {
        this.totalAnticiposAdeudados = totalAnticiposAdeudados;
    }

    public double getTotalAnticiposAdeudados() {
        return totalAnticiposAdeudados;
    }

    public void setPagoAnticipado(double pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }

    public double getPagoAnticipado() {
        return pagoAnticipado;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getCapital() {
        return capital;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getInteres() {
        return interes;
    }

    public void setTotalAFinanciar(double totalAFinanciar) {
        this.totalAFinanciar = totalAFinanciar;
    }

    public double getTotalAFinanciar() {
        return totalAFinanciar;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setDiaVencimiento(int diaVencimiento) {
        this.diaVencimiento = diaVencimiento;
    }

    public int getDiaVencimiento() {
        return diaVencimiento;
    }

    public void setPorcentajeInteresFinanciacion(double porcentajeInteresFinanciacion) {
        this.porcentajeInteresFinanciacion = porcentajeInteresFinanciacion;
    }

    public double getPorcentajeInteresFinanciacion() {
        return porcentajeInteresFinanciacion;
    }

    public void setMontoInteresFinanciacion(double montoInteresFinanciacion) {
        this.montoInteresFinanciacion = montoInteresFinanciacion;
    }

    public double getMontoInteresFinanciacion() {
        return montoInteresFinanciacion;
    }

    public void setValorPrimerCuota(double valorPrimerCuota) {
        this.valorPrimerCuota = valorPrimerCuota;
    }

    public double getValorPrimerCuota() {
        return valorPrimerCuota;
    }

    public void setValorCuotasRestantes(double valorCuotasRestantes) {
        this.valorCuotasRestantes = valorCuotasRestantes;
    }

    public double getValorCuotasRestantes() {
        return valorCuotasRestantes;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void retrieveData(TipoPlanDePago _tipoPlanDePago, Bien _bien, Bonificacion _bonificacion, double _entrega) {
        ResultSet configPlanDePago = LibSQL.exFunction("taxes.getConfiguracionPlanDePago", _tipoPlanDePago.getIdTipoImpuesto() + "," + _bien.getIdBien() + ",'" + _tipoPlanDePago.getFechaInicio() + "','" + _tipoPlanDePago.getFechaFin() + "'" + "," + _bonificacion.getPorcentaje() + "," + pagoContado + "," + _entrega);
        try {
            if (configPlanDePago.next()) {
                BigDecimal montoCondonacionInt = new BigDecimal("0");
                BigDecimal valorNetoAnticipoAct = new BigDecimal("0");
                menorAnio = configPlanDePago.getInt("menoranio");
                mayorAnio = configPlanDePago.getInt("mayoranio");
                menorAnticipo = configPlanDePago.getInt("menoranticipo");
                mayorAnticipo = configPlanDePago.getInt("mayorAnticipo");
                cantidadAnticipos = configPlanDePago.getInt("cantidad");
                totalAnticiposAdeudados = configPlanDePago.getDouble("montoTotal");
                capital = configPlanDePago.getDouble("montoBase");
                interes = configPlanDePago.getDouble("interes");
                capitalPuro = configPlanDePago.getDouble("montoBasePuro");
                interesPuro = configPlanDePago.getDouble("interesPuro");
                totalAnticiposAdeudadosPuro = configPlanDePago.getDouble("montoTotalPuro");
                totalAFinanciar = configPlanDePago.getDouble("montoBase");
                valorAnticipoActual = configPlanDePago.getDouble("montoAnticipoActual");
                valorDescuentoAnticipoActual = configPlanDePago.getDouble("descuentoAnticipoActual");
                porcentajeDescuentoPagoContado = configPlanDePago.getDouble("porcDescPagoCtdo");
                porcentajeCondonacionIntereses = _tipoPlanDePago.getPorcentajeCondonacionIntereses();
                porcentajeInteresFinanciacion = _tipoPlanDePago.getPorcentajeInteresCuota();
                menorAnioxContado = configPlanDePago.getInt("menoranioxcontado");
                ;
                mayorAnioxContado = configPlanDePago.getInt("mayoranioxcontado");
                menorAnticipoxContado = configPlanDePago.getInt("menoranticipoxcontado");
                mayorAnticipoxContado = configPlanDePago.getInt("mayoranticipoxcontado");
                cantidadAnticiposxContado = configPlanDePago.getInt("cantidadAnticiposxcontado");
                capitalPuroxContado = configPlanDePago.getDouble("montoBasePuroxContado");
                interesPuroxContado = configPlanDePago.getDouble("interespuroxcontado");
                totalAnticiposAdeudadosPuroxContado = configPlanDePago.getDouble("montototalPuroxContado");
                BigDecimal montoBonifCtdo = new BigDecimal("0");
                montoBonifCtdo = montoBonifCtdo.add(new BigDecimal(bonificacion.getPorcentaje()));
                montoBonifCtdo = montoBonifCtdo.multiply(new BigDecimal(totalAnticiposAdeudadosPuroxContado));
                montoBonificacionCtdo = montoBonifCtdo.doubleValue();
                BigDecimal montoDescuentoPagoCtdo = new BigDecimal("0");
                montoDescuentoPagoCtdo = montoDescuentoPagoCtdo.add(new BigDecimal(totalAnticiposAdeudadosPuroxContado));
                montoDescuentoPagoCtdo = montoDescuentoPagoCtdo.subtract(new BigDecimal(montoBonificacionCtdo));
                montoDescuentoPagoCtdo = montoDescuentoPagoCtdo.multiply(new BigDecimal(porcentajeDescuentoPagoContado));
                montoDescuentoPagoCtdo = montoDescuentoPagoCtdo.divide(new BigDecimal("100"));
                montoDescuentoPagoContado = montoDescuentoPagoCtdo.doubleValue();
                porcentajeBonificacion = bonificacion.getPorcentaje();
                BigDecimal montoBonif = new BigDecimal("0");
                montoBonif = montoBonif.add(new BigDecimal(totalAnticiposAdeudadosPuro));
                montoBonif = montoBonif.multiply(new BigDecimal("" + bonificacion.getPorcentaje()));
                montoBonificacion = montoBonif.doubleValue();
                subTotalEstadoCuenta = capitalPuro + interesPuro - montoBonificacion;
                if (pagoContado) {
                    BigDecimal montoBonifPagoCtdo = new BigDecimal("0");
                    BigDecimal totalAPagar = new BigDecimal("0");
                    montoCondonacionInt = new BigDecimal("0");
                    montoCondonacionInt = montoCondonacionInt.add(new BigDecimal(totalAnticiposAdeudadosPuro));
                    montoCondonacionInt = montoCondonacionInt.multiply(new BigDecimal(bonificacion.getPorcentaje()));
                    montoCondonacionIntereses = montoCondonacionInt.doubleValue();
                    montoBonifPagoCtdo = montoBonifPagoCtdo.add(new BigDecimal(totalAnticiposAdeudados));
                    montoBonifPagoCtdo = montoBonifPagoCtdo.multiply(new BigDecimal(porcentajeDescuentoPagoContado));
                    montoBonifPagoCtdo = montoBonifPagoCtdo.divide(new BigDecimal("100"));
                    montoDescuentoPagoContado = montoBonifPagoCtdo.doubleValue();
                    totalAPagar = totalAPagar.add(new BigDecimal(totalAnticiposAdeudados));
                    totalAPagar = totalAPagar.subtract(new BigDecimal(montoDescuentoPagoContado));
                    total = totalAPagar.doubleValue();
                    porcentajeCondonacionIntereses = 0.0;
                    montoCondonacionIntereses = 0.0;
                    porcentajeInteresFinanciacion = 0.0;
                    montoInteresFinanciacion = 0.0;
                }
                if (pagoAnticipado > 0) {
                    cantAnticiposCubiertosEntrega = configPlanDePago.getInt("cantanticiposcubiertos");
                    menorAnioEntrega = configPlanDePago.getInt("menoranioentrega");
                    menorAnticipoEntrega = configPlanDePago.getInt("menoranticipoentrega");
                    mayorAnioEntrega = configPlanDePago.getInt("mayoranioentrega");
                    mayorAnticipoEntrega = configPlanDePago.getInt("mayoranticipoentrega");
                    anticipoActualEntrega = configPlanDePago.getInt("numeroanticipoact");
                    anioAnticipoActualEntrega = configPlanDePago.getInt("anioanticipoact");
                    menorAnioPPEntrega = configPlanDePago.getInt("menoranioppentrega");
                    menorAnticipoPPEntrega = configPlanDePago.getInt("menoranticipoppentrega");
                    mayorAnioPPEntrega = configPlanDePago.getInt("mayoranioppentrega");
                    mayorAnticipoPPEntrega = configPlanDePago.getInt("mayoranticipoppentrega");
                    montoBaseCubiertoEntrega = configPlanDePago.getDouble("montobasecubiertoentrega");
                    interesBaseCubiertoEntrega = configPlanDePago.getDouble("montointerescubiertoentrega");
                    totalBaseCubiertoEntrega = configPlanDePago.getDouble("montototalcubiertoentrega");
                    montoBaseCubiertoEntregaSinDto = configPlanDePago.getDouble("montobasecubiertoentregasindto");
                    interesBaseCubiertoEntregaSinDto = configPlanDePago.getDouble("montointerescubiertoentregasindto");
                    totalBaseCubiertoEntregaSinDto = configPlanDePago.getDouble("montototalcubiertoentregasindto");
                    totalAnticiposAdeudadosEntrega = configPlanDePago.getDouble("totalanticiposadeudadosentrega");
                    saldoEntrega = configPlanDePago.getDouble("saldoentrega");
                    pagoAnticipado = configPlanDePago.getDouble("entrega");
                    montoBasePPEntrega = configPlanDePago.getDouble("montobaseppentrega");
                    interesBasePPEntrega = configPlanDePago.getDouble("montointeresppentrega");
                    totalBasePPEntrega = configPlanDePago.getDouble("totalanticiposadeudadosentrega");
                    montoBasePPEntregaSinDto = configPlanDePago.getDouble("montobaseppentregasindto");
                    interesBasePPEntregaSinDto = configPlanDePago.getDouble("montointeresppentregasindto");
                    totalBasePPEntregaSinDto = configPlanDePago.getDouble("totalanticiposadeudadosPPentregasindto");
                    montoCondonacionInt = new BigDecimal("0");
                    if (_bonificacion.getPorcentaje() == 0.00) {
                        montoCondonacionInt = montoCondonacionInt.add(new BigDecimal("" + interesBasePPEntrega));
                    } else {
                        montoCondonacionInt = montoCondonacionInt.add(new BigDecimal("" + interes));
                    }
                    montoCondonacionInt = montoCondonacionInt.multiply(new BigDecimal("" + porcentajeCondonacionIntereses));
                    montoCondonacionInt = Format.toDouble(montoCondonacionInt.divide(new BigDecimal("100")).toString());
                    montoCondonacionIntereses = montoCondonacionInt.doubleValue();
                    valorNetoAnticipoAct = new BigDecimal("0");
                    valorNetoAnticipoAct = valorNetoAnticipoAct.add(new BigDecimal("" + valorAnticipoActual));
                    valorNetoAnticipoAct = valorNetoAnticipoAct.subtract(new BigDecimal("" + valorDescuentoAnticipoActual));
                    valorNetoAnticipoAct = Format.toDouble(valorNetoAnticipoAct.toString());
                    valorNetoAnticipoActual = valorNetoAnticipoAct.doubleValue();
                } else {
                    valorNetoAnticipoAct = new BigDecimal("0");
                    valorNetoAnticipoAct = valorNetoAnticipoAct.add(new BigDecimal("" + valorAnticipoActual));
                    valorNetoAnticipoAct = valorNetoAnticipoAct.subtract(new BigDecimal("" + valorDescuentoAnticipoActual));
                    valorNetoAnticipoAct = Format.toDouble(valorNetoAnticipoAct.toString());
                    valorNetoAnticipoActual = valorNetoAnticipoAct.doubleValue();
                    montoCondonacionInt = new BigDecimal("0");
                    montoCondonacionInt = montoCondonacionInt.add(new BigDecimal("" + interes));
                    montoCondonacionInt = montoCondonacionInt.multiply(new BigDecimal("" + porcentajeCondonacionIntereses));
                    montoCondonacionInt = Format.toDouble(montoCondonacionInt.divide(new BigDecimal("100")).toString());
                    montoCondonacionIntereses = montoCondonacionInt.doubleValue();
                    anticipoActualEntrega = configPlanDePago.getInt("numeroanticipoact");
                    anioAnticipoActualEntrega = configPlanDePago.getInt("anioanticipoact");
                }
                totalPagoxContado = capitalPuroxContado + interesPuroxContado - montoBonificacionCtdo - montoDescuentoPagoContado;
                totalAPagarCuotas = capitalPuro + interesPuro - montoCondonacionIntereses - montoBonificacion + montoInteresFinanciacion;
                generado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            generado = false;
        }
    }

    public void setMenorAnticipo(int menorAnticipo) {
        this.menorAnticipo = menorAnticipo;
    }

    public int getMenorAnticipo() {
        return menorAnticipo;
    }

    public void setMayorAnticipo(int mayorAnticipo) {
        this.mayorAnticipo = mayorAnticipo;
    }

    public int getMayorAnticipo() {
        return mayorAnticipo;
    }

    public void setCapitalPuro(double capitalPuro) {
        this.capitalPuro = capitalPuro;
    }

    public double getCapitalPuro() {
        return capitalPuro;
    }

    public void setInteresPuro(double interesPuro) {
        this.interesPuro = interesPuro;
    }

    public double getInteresPuro() {
        return interesPuro;
    }

    public void setTotalAnticiposAdeudadosPuro(double totalAnticiposAdeudadosPuro) {
        this.totalAnticiposAdeudadosPuro = totalAnticiposAdeudadosPuro;
    }

    public double getTotalAnticiposAdeudadosPuro() {
        return totalAnticiposAdeudadosPuro;
    }

    public void setValorAnticipoActual(double valorAnticipoActual) {
        this.valorAnticipoActual = valorAnticipoActual;
    }

    public double getValorAnticipoActual() {
        return valorAnticipoActual;
    }

    public void setTotalAPagar(double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public double getTotalAPagar() {
        return totalAPagar;
    }

    public void setValorDescuentoAnticipoActual(double valorDescuentoAnticipoActual) {
        this.valorDescuentoAnticipoActual = valorDescuentoAnticipoActual;
    }

    public double getValorDescuentoAnticipoActual() {
        return valorDescuentoAnticipoActual;
    }

    public void setPagoContado(boolean pagoContado) {
        this.pagoContado = pagoContado;
    }

    public boolean isPagoContado() {
        return pagoContado;
    }

    public void setPorcentajeDescuentoPagoContado(double porcentajeDescuentoPagoContado) {
        this.porcentajeDescuentoPagoContado = porcentajeDescuentoPagoContado;
    }

    public double getPorcentajeDescuentoPagoContado() {
        return porcentajeDescuentoPagoContado;
    }

    public void setMontoDescuentoPagoContado(double montoDescuentoPagoContado) {
        this.montoDescuentoPagoContado = montoDescuentoPagoContado;
    }

    public double getMontoDescuentoPagoContado() {
        return montoDescuentoPagoContado;
    }

    public void setBonificacion(Bonificacion bonificacion) {
        this.bonificacion = bonificacion;
    }

    public Bonificacion getBonificacion() {
        return bonificacion;
    }

    public void setPorcentajeBonificacion(double porcentajeBonificacion) {
        this.porcentajeBonificacion = porcentajeBonificacion;
    }

    public double getPorcentajeBonificacion() {
        return porcentajeBonificacion;
    }

    public void setMontoBonificacion(double montoBonificacion) {
        this.montoBonificacion = montoBonificacion;
    }

    public double getMontoBonificacion() {
        return montoBonificacion;
    }

    public void setSubTotalEstadoCuenta(double subTotalEstadoCuenta) {
        this.subTotalEstadoCuenta = subTotalEstadoCuenta;
    }

    public double getSubTotalEstadoCuenta() {
        return subTotalEstadoCuenta;
    }

    public void setMenorAnioxContado(int menorAnioxContado) {
        this.menorAnioxContado = menorAnioxContado;
    }

    public int getMenorAnioxContado() {
        return menorAnioxContado;
    }

    public void setMayorAnioxContado(int mayorAnioxContado) {
        this.mayorAnioxContado = mayorAnioxContado;
    }

    public int getMayorAnioxContado() {
        return mayorAnioxContado;
    }

    public void setMenorMesxContado(int menorMesxContado) {
        this.menorMesxContado = menorMesxContado;
    }

    public int getMenorMesxContado() {
        return menorMesxContado;
    }

    public void setMayorMesxContado(int mayorMesxContado) {
        this.mayorMesxContado = mayorMesxContado;
    }

    public int getMayorMesxContado() {
        return mayorMesxContado;
    }

    public void setMenorAnticipoxContado(int menorAnticipoxContado) {
        this.menorAnticipoxContado = menorAnticipoxContado;
    }

    public int getMenorAnticipoxContado() {
        return menorAnticipoxContado;
    }

    public void setMayorAnticipoxContado(int mayorAnticipoxContado) {
        this.mayorAnticipoxContado = mayorAnticipoxContado;
    }

    public int getMayorAnticipoxContado() {
        return mayorAnticipoxContado;
    }

    public void setCantidadAnticiposxContado(int cantidadAnticiposxContado) {
        this.cantidadAnticiposxContado = cantidadAnticiposxContado;
    }

    public int getCantidadAnticiposxContado() {
        return cantidadAnticiposxContado;
    }

    public void setCapitalPuroxContado(double capitalPuroxContado) {
        this.capitalPuroxContado = capitalPuroxContado;
    }

    public double getCapitalPuroxContado() {
        return capitalPuroxContado;
    }

    public void setInteresPuroxContado(double interesPuroxContado) {
        this.interesPuroxContado = interesPuroxContado;
    }

    public double getInteresPuroxContado() {
        return interesPuroxContado;
    }

    public void setTotalAnticiposAdeudadosPuroxContado(double totalAnticiposAdeudadosPuroxContado) {
        this.totalAnticiposAdeudadosPuroxContado = totalAnticiposAdeudadosPuroxContado;
    }

    public double getTotalAnticiposAdeudadosPuroxContado() {
        return totalAnticiposAdeudadosPuroxContado;
    }

    public void setTotalPagoxContado(double totalPagoxContado) {
        this.totalPagoxContado = totalPagoxContado;
    }

    public double getTotalPagoxContado() {
        return totalPagoxContado;
    }

    public void setTotalAPagarCuotas(double totalAPagarCuotas) {
        this.totalAPagarCuotas = totalAPagarCuotas;
    }

    public double getTotalAPagarCuotas() {
        return totalAPagarCuotas;
    }

    public void setMontoBonificacionCtdo(double montoBonificacionCtdo) {
        this.montoBonificacionCtdo = montoBonificacionCtdo;
    }

    public double getMontoBonificacionCtdo() {
        return montoBonificacionCtdo;
    }

    public void setCantAnticiposCubiertosEntrega(int cantAnticiposCubiertosEntrega) {
        this.cantAnticiposCubiertosEntrega = cantAnticiposCubiertosEntrega;
    }

    public int getCantAnticiposCubiertosEntrega() {
        return cantAnticiposCubiertosEntrega;
    }

    public void setMenorAnioEntrega(int menorAnioEntrega) {
        this.menorAnioEntrega = menorAnioEntrega;
    }

    public int getMenorAnioEntrega() {
        return menorAnioEntrega;
    }

    public void setMenorAnticipoEntrega(int menorAnticipoEntrega) {
        this.menorAnticipoEntrega = menorAnticipoEntrega;
    }

    public int getMenorAnticipoEntrega() {
        return menorAnticipoEntrega;
    }

    public void setMayorAnioEntrega(int mayorAnioEntrega) {
        this.mayorAnioEntrega = mayorAnioEntrega;
    }

    public int getMayorAnioEntrega() {
        return mayorAnioEntrega;
    }

    public void setMayorAnticipoEntrega(int mayorAnticipoEntrega) {
        this.mayorAnticipoEntrega = mayorAnticipoEntrega;
    }

    public int getMayorAnticipoEntrega() {
        return mayorAnticipoEntrega;
    }

    public void setAnticipoActualEntrega(int anticipoActualEntrega) {
        this.anticipoActualEntrega = anticipoActualEntrega;
    }

    public int getAnticipoActualEntrega() {
        return anticipoActualEntrega;
    }

    public void setAnioAnticipoActualEntrega(int anioAnticipoActualEntrega) {
        this.anioAnticipoActualEntrega = anioAnticipoActualEntrega;
    }

    public int getAnioAnticipoActualEntrega() {
        return anioAnticipoActualEntrega;
    }

    public void setMenorAnioPPEntrega(int menorAnioPPEntrega) {
        this.menorAnioPPEntrega = menorAnioPPEntrega;
    }

    public int getMenorAnioPPEntrega() {
        return menorAnioPPEntrega;
    }

    public void setMenorAnticipoPPEntrega(int menorAnticipoPPEntrega) {
        this.menorAnticipoPPEntrega = menorAnticipoPPEntrega;
    }

    public int getMenorAnticipoPPEntrega() {
        return menorAnticipoPPEntrega;
    }

    public void setMayorAnioPPEntrega(int mayorAnioPPEntrega) {
        this.mayorAnioPPEntrega = mayorAnioPPEntrega;
    }

    public int getMayorAnioPPEntrega() {
        return mayorAnioPPEntrega;
    }

    public void setMayorAnticipoPPEntrega(int mayorAnticipoPPEntrega) {
        this.mayorAnticipoPPEntrega = mayorAnticipoPPEntrega;
    }

    public int getMayorAnticipoPPEntrega() {
        return mayorAnticipoPPEntrega;
    }

    public void setMontoBaseCubiertoEntrega(double montoBaseCubiertoEntrega) {
        this.montoBaseCubiertoEntrega = montoBaseCubiertoEntrega;
    }

    public double getMontoBaseCubiertoEntrega() {
        return montoBaseCubiertoEntrega;
    }

    public void setInteresBaseCubiertoEntrega(double interesBaseCubiertoEntrega) {
        this.interesBaseCubiertoEntrega = interesBaseCubiertoEntrega;
    }

    public double getInteresBaseCubiertoEntrega() {
        return interesBaseCubiertoEntrega;
    }

    public void setTotalBaseCubiertoEntrega(double totalBaseCubiertoEntrega) {
        this.totalBaseCubiertoEntrega = totalBaseCubiertoEntrega;
    }

    public double getTotalBaseCubiertoEntrega() {
        return totalBaseCubiertoEntrega;
    }

    public void setSaldoEntrega(double saldoEntrega) {
        this.saldoEntrega = saldoEntrega;
    }

    public double getSaldoEntrega() {
        return saldoEntrega;
    }

    public void setTotalAnticiposAdeudadosEntrega(double totalAnticiposAdeudadosEntrega) {
        this.totalAnticiposAdeudadosEntrega = totalAnticiposAdeudadosEntrega;
    }

    public double getTotalAnticiposAdeudadosEntrega() {
        return totalAnticiposAdeudadosEntrega;
    }

    public void setMontoBasePPEntrega(double montoBasePPEntrega) {
        this.montoBasePPEntrega = montoBasePPEntrega;
    }

    public double getMontoBasePPEntrega() {
        return montoBasePPEntrega;
    }

    public void setInteresBasePPEntrega(double interesBasePPEntrega) {
        this.interesBasePPEntrega = interesBasePPEntrega;
    }

    public double getInteresBasePPEntrega() {
        return interesBasePPEntrega;
    }

    public void setTotalBasePPEntrega(double totalBasePPEntrega) {
        this.totalBasePPEntrega = totalBasePPEntrega;
    }

    public double getTotalBasePPEntrega() {
        return totalBasePPEntrega;
    }

    public void setTotalAPagarSinEntrega(double totalAPAgarSinEntrega) {
        this.totalAPAgarSinEntrega = totalAPAgarSinEntrega;
    }

    public double getTotalAPagarSinEntrega() {
        totalAPAgarSinEntrega = (new BigDecimal("" + (valorPrimerCuota + valorNetoAnticipoActual))).doubleValue();
        return totalAPAgarSinEntrega;
    }

    public void setTotalAPagarConEntrega(double totalAPagarConEntrega) {
        this.totalAPagarConEntrega = totalAPagarConEntrega;
    }

    public double getTotalAPagarConEntrega() {
        BigDecimal total = new BigDecimal("0");
        total.add(new BigDecimal(valorAnticipoActual));
        total.add(new BigDecimal(totalBaseCubiertoEntrega));
        totalAPagarConEntrega = total.doubleValue();
        totalAPagarConEntrega = (new BigDecimal("" + (valorNetoAnticipoActual + totalBaseCubiertoEntrega))).doubleValue();
        return totalAPagarConEntrega;
    }

    public void setMontoTotalCuotas(double montoTotalCuotas) {
        this.montoTotalCuotas = montoTotalCuotas;
    }

    public double getMontoTotalCuotas() {
        return montoTotalCuotas;
    }

    public void setValorNetoAnticipoActual(double valorNetoAnticipoActual) {
        this.valorNetoAnticipoActual = valorNetoAnticipoActual;
    }

    public double getValorNetoAnticipoActual() {
        return valorNetoAnticipoActual;
    }

    public void setMontoBaseCubiertoEntregaSinDto(double montoBaseCubiertoEntregaSinDto) {
        this.montoBaseCubiertoEntregaSinDto = montoBaseCubiertoEntregaSinDto;
    }

    public double getMontoBaseCubiertoEntregaSinDto() {
        return montoBaseCubiertoEntregaSinDto;
    }

    public void setInteresBaseCubiertoEntregaSinDto(double interesBaseCubiertoEntregaSinDto) {
        this.interesBaseCubiertoEntregaSinDto = interesBaseCubiertoEntregaSinDto;
    }

    public double getInteresBaseCubiertoEntregaSinDto() {
        return interesBaseCubiertoEntregaSinDto;
    }

    public void setTotalBaseCubiertoEntregaSinDto(double totalBaseCubiertoEntregaSinDto) {
        this.totalBaseCubiertoEntregaSinDto = totalBaseCubiertoEntregaSinDto;
    }

    public double getTotalBaseCubiertoEntregaSinDto() {
        return totalBaseCubiertoEntregaSinDto;
    }

    public void setMontoBasePPEntregaSinDto(double montoBasePPEntregaSinDto) {
        this.montoBasePPEntregaSinDto = montoBasePPEntregaSinDto;
    }

    public double getMontoBasePPEntregaSinDto() {
        return montoBasePPEntregaSinDto;
    }

    public void setInteresBasePPEntregaSinDto(double interesBasePPEntregaSinDto) {
        this.interesBasePPEntregaSinDto = interesBasePPEntregaSinDto;
    }

    public double getInteresBasePPEntregaSinDto() {
        return interesBasePPEntregaSinDto;
    }

    public void setTotalBasePPEntregaSinDto(double totalBasePPEntregaSinDto) {
        this.totalBasePPEntregaSinDto = totalBasePPEntregaSinDto;
    }

    public double getTotalBasePPEntregaSinDto() {
        return totalBasePPEntregaSinDto;
    }

    public boolean isGenerado() {
        return generado;
    }
}

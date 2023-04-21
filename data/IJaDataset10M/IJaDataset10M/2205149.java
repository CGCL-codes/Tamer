package Controller;

import Model.Congreso;
import Model.Pedido;
import Servicio.Proxy;
import Servicio.Impresora;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SegPedidoController {

    private ArrayList<Congreso> mCongresos;

    private ArrayList<Pedido> mPedidos;

    private Congreso mCongreso;

    private Pedido mPedido;

    private Pedido pedidoElim;

    public SegPedidoController() {
        mCongresos = new ArrayList();
        pedidoElim = null;
    }

    public void imprimirTren(ArrayList<String> DVD, ArrayList<String> Cantidad) {
        PrinterJob job = PrinterJob.getPrinterJob();
        Impresora imp = new Impresora();
        for (int i = 0; i < DVD.size(); i++) {
            imp.getCadenaImpresora().add(DVD.get(i).toString());
            imp.getCadenaImpresora2().add(Cantidad.get(i).toString());
        }
        java.awt.print.PageFormat format = new java.awt.print.PageFormat();
        java.awt.print.Paper paper = new java.awt.print.Paper();
        int largopapel = 28 * DVD.size() + 80;
        paper.setSize(200, largopapel);
        paper.setImageableArea(1, 1, 190, largopapel - 10);
        format.setPaper(paper);
        format.setOrientation(PageFormat.PORTRAIT);
        job.setPrintable(imp, format);
        try {
            job.print();
        } catch (PrinterException ex) {
            Logger.getLogger(RPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirRecibo(String nroComp, String nombre, String fecha, String total, String cong) {
        PrinterJob job = PrinterJob.getPrinterJob();
        Impresora imp = new Impresora();
        String diaI = fecha.substring(8, 10);
        String mesI = fecha.substring(5, 7);
        String anioI = fecha.substring(0, 4);
        String fecha2 = diaI + "/" + mesI + "/" + anioI;
        imp.getCadenaImpresora3().add(nroComp);
        imp.getCadenaImpresora3().add(nombre);
        imp.getCadenaImpresora3().add(fecha2);
        imp.getCadenaImpresora3().add(total);
        imp.getCadenaImpresora3().add(cong);
        java.awt.print.PageFormat format = new java.awt.print.PageFormat();
        java.awt.print.Paper paper = new java.awt.print.Paper();
        paper.setSize(100, 1000);
        paper.setImageableArea(0, 0, 200, 1000);
        format.setPaper(paper);
        job.setPrintable(imp, format);
        try {
            job.print();
        } catch (PrinterException ex) {
            Logger.getLogger(RPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Congreso> getCongresos() {
        return mCongresos;
    }

    public void setCongresos(ArrayList<Congreso> val) {
        this.mCongresos = val;
    }

    public Congreso getCongreso() {
        return mCongreso;
    }

    public void setCongreso(Congreso val) {
        this.mCongreso = val;
    }

    public Pedido getPedido() {
        return mPedido;
    }

    public void setPedido(Pedido val) {
        this.mPedido = val;
    }

    public ArrayList<Pedido> getPedidos() {
        return mPedidos;
    }

    public void setPedidos(ArrayList<Pedido> val) {
        this.mPedidos = val;
    }

    public void setPedidoElim(Pedido val) {
        this.pedidoElim = val;
    }

    public Pedido getPedidoElim() {
        return pedidoElim;
    }

    /**Carga el arrayList con los Congresos*/
    public void cargarCongresos(boolean vent) {
        if (vent) {
            this.setCongresos(Proxy.obtenerCongresos());
        } else this.setCongresos(Proxy.obtenerCongresosConEnvios());
        mCongresos.add(mCongresos.size() - 1, new Congreso("TODOS", "TODOS", "00-00-0000 00:00:00", "00-00-0000 00:00:00", 0, 0, "", "", "", "", 0, 0, 0, 0, "", "", "", 0, 0));
    }

    /**Carga el arrayList con los Pedidos del congreso seleccionado*/
    public void cargarPedidos(boolean vent) {
        if (mCongreso.getIniciales().compareTo("TODOS") == 0 && mCongreso.getFechaInicio().compareTo("00-00-0000 00:00:00") == 0) {
            ArrayList<Pedido> auxPeds = new ArrayList<Pedido>();
            if (vent) {
                for (int j = 0; j < mCongresos.size() - 1; j++) {
                    this.setPedidos(Proxy.obtenerPedidos(mCongresos.get(j)).getPedido());
                    for (int i = 0; i < mPedidos.size(); i++) if (mPedidos.get(i).getEnvio().compareTo("1") != 0) {
                        auxPeds.add(mPedidos.get(i));
                    }
                }
                this.setPedidos(auxPeds);
            } else this.setPedidos(Proxy.obtenerEnvios());
        } else {
            this.setPedidos(Proxy.obtenerPedidos(mCongreso).getPedido());
            if (vent) {
                for (int i = 0; i < mPedidos.size(); i++) if (mPedidos.get(i).getEnvio().compareTo("1") == 0 || mPedidos.get(i).getEnvio().compareTo("si") == 0) {
                    mPedidos.remove(i);
                    i--;
                }
            } else {
                for (int i = 0; i < mPedidos.size(); i++) if (mPedidos.get(i).getEnvio().compareTo("1") != 0 && mPedidos.get(i).getEnvio().compareTo("si") != 0) {
                    mPedidos.remove(i);
                    i--;
                }
            }
        }
    }

    public Congreso cargarCongreso() {
        return null;
    }

    public void cambioEstadoPedido() {
        Proxy.modificarPedido(mPedido);
    }

    public void eliminarPedidoSeleccionado() {
        Proxy.eliminarPedido(pedidoElim);
        if (avisarEnQueCaja(mCongreso, pedidoElim) == true) {
            mCongreso.setStockDvd(mCongreso.getStockDvd() + pedidoElim.getDVDs().size());
            Proxy.modificarCongreso(mCongreso);
        } else {
            Congreso oficina = null;
            for (int i = mCongresos.size() - 1; i >= 0; i--) {
                if (mCongresos.get(i).getIniciales().compareTo("OFICE") == 0) {
                    oficina = mCongresos.get(i);
                    break;
                }
            }
            oficina.setStockDvd(oficina.getStockDvd() + pedidoElim.getDVDs().size());
            Proxy.modificarCongreso(oficina);
        }
    }

    public void imprimirPendiente() {
    }

    public void imprimirRecibo() {
    }

    private boolean avisarEnQueCaja(Congreso con, Pedido ped) {
        boolean error = false;
        Calendar calendar = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();
        Calendar fechaAct = Calendar.getInstance();
        Calendar fechaIni = Calendar.getInstance();
        int anioI = Integer.parseInt(con.getFechaInicio().substring(0, 4));
        int mesI = Integer.parseInt(con.getFechaInicio().substring(5, 7));
        int diaI = Integer.parseInt(con.getFechaInicio().substring(8, 10));
        int anioF = Integer.parseInt(con.getFechaFin().substring(0, 4));
        int mesF = Integer.parseInt(con.getFechaFin().substring(5, 7));
        int diaF = Integer.parseInt(con.getFechaFin().substring(8, 10));
        int anioA = Integer.parseInt(ped.getFecha().substring(0, 4));
        int mesA = Integer.parseInt(ped.getFecha().substring(5, 7));
        int diaA = Integer.parseInt(ped.getFecha().substring(8, 10));
        fechaIni.set(anioI, mesI, diaI);
        fechaFin.set(anioF, mesF, diaF);
        fechaAct.set(anioA, mesA, diaA);
        if (fechaIni.compareTo(fechaAct) <= 0 && fechaFin.compareTo(fechaAct) >= 0) error = true;
        return error;
    }
}

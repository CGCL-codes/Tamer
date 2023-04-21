package Servicio;

import Model.Congreso;

public class ManejadorCongreso extends ManejadorAbstracto {

    public ManejadorCongreso() {
    }

    protected String consulta() {
        String aux = "select * from Congres ";
        switch(opcion) {
            case 0:
                aux = aux + "where estado not like 'Eliminado' order by Fecha_inicio desc";
                break;
            case 1:
                aux = aux + "where Iniciales ='" + valor + "' and Fecha_Inicio='" + valor2 + "'";
                break;
            case 2:
                aux = aux + "join Pedids on (Iniciales_Congreso=Iniciales and Fecha_inicio=Fecha_Congreso)" + " where Pedids.Estado in ('Pendiente','Listo') and envio=1 " + "GROUP BY Nombre,Iniciales,Fecha_Inicio,Fecha_Fin,Precio_DVD,Cantidad_DVD,Localidad,Provincia,Pais,Congres.estado";
                break;
            case 3:
                aux = "select Nombre, Iniciales, Fecha_Inicio, Fecha_Fin, Precio_DVD, Cantidad_DVD, " + "Localidad, Provincia, Pais, Estado, stock, malos, " + " Precio_DVD_M2, Precio_DVD_M3, Simb_M1, Simb_M2, Simb_M3, boxes, boxesMalos " + "from congres C join pedidos P on " + "(C.Iniciales=P.iniciales_Congreso) and (C.Fecha_Inicio = P.Fecha_Congreso) " + "where  (strftime('%W','now')-strftime('%W',P.Fecha) )<6 " + "group by  Nombre, Iniciales, Fecha_Inicio, Fecha_Fin, Precio_DVD, Cantidad_DVD, " + "Localidad, Provincia, Pais, Estado " + "Union " + "select Nombre, Iniciales, Fecha_Inicio, Fecha_Fin, Precio_DVD, Cantidad_DVD, " + "Localidad, Provincia, Pais, Estado, stock, malos ," + " Precio_DVD_M2, Precio_DVD_M3, Simb_M1, Simb_M2, Simb_M3, boxes, boxesMalos " + "from congres C left join pedidos P on " + "(C.Iniciales=P.iniciales_Congreso) and (C.Fecha_Inicio = P.Fecha_Congreso) " + "where P.Iniciales_Congreso is null " + "Order by Fecha_Inicio desc";
                break;
            case 4:
                aux = aux + " order by Fecha_inicio desc";
        }
        return aux;
    }

    protected Object nuevo() {
        Object aux = null;
        try {
            aux = new Congreso(resultSet.getString("Nombre"), resultSet.getString("Iniciales"), resultSet.getString("Fecha_Inicio"), resultSet.getString("Fecha_Fin"), resultSet.getFloat("Precio_DVD"), resultSet.getInt("Cantidad_DVD"), resultSet.getString("Localidad"), resultSet.getString("Provincia"), resultSet.getString("Pais"), resultSet.getString(10), resultSet.getInt("stock"), resultSet.getInt("malos"), resultSet.getFloat("Precio_DVD_M2"), resultSet.getFloat("Precio_DVD_M3"), resultSet.getString("Simb_M1"), resultSet.getString("Simb_M2"), resultSet.getString("Simb_M3"), resultSet.getInt("boxes"), resultSet.getInt("boxesMalos"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aux;
    }

    protected String table() {
        return "Congreso(Nombre, Iniciales, Fecha_Inicio, Fecha_Fin, Precio_DVD, " + "Precio_DVD_M2 , Precio_DVD_M3 , Simb_M1,  Simb_M2, Simb_M3, " + "Cantidad_DVD,Id_Localidad,Id_Provincia,Id_Pais,Id_Estado,stock,malos,boxes,boxesMalos)";
    }

    protected String values(Object objeto) {
        String aux;
        Congreso congreso = (Congreso) objeto;
        int auxIdEstado = Facade.obtenerIdFijo(congreso.getEstado(), 0);
        int auxIdLocalidad = Facade.obtenerIdExpandible(congreso.getLocalidad(), 1);
        int auxIdProvincia = Facade.obtenerIdExpandible(congreso.getProvincia(), 2);
        int auxIdPais = Facade.obtenerIdExpandible(congreso.getPais(), 3);
        String auxSimbM2 = Facade.obtenerSimbMoneda(congreso.getSimbM2());
        String auxSimbM3 = Facade.obtenerSimbMoneda(congreso.getSimbM3());
        aux = "('" + congreso.getNombreCong() + "','" + congreso.getIniciales() + "','" + congreso.getFechaInicio() + "','" + congreso.getFechaFin() + "'," + congreso.getPrecioDvd() + "," + congreso.getPrecioDvdM2() + "," + congreso.getPrecioDvdM3() + ",'" + congreso.getSimbM1() + "','" + auxSimbM2 + "','" + auxSimbM3 + "'," + congreso.getCantDvd() + "," + auxIdLocalidad + "," + auxIdProvincia + "," + auxIdPais + "," + auxIdEstado + "," + congreso.getStockDvd() + "," + congreso.getCantMalos() + "," + congreso.getCantBoxes() + "," + congreso.getCantBoxesMalos() + ")";
        return aux;
    }

    protected String table_u() {
        return "Congreso";
    }

    protected String campos_u(Object objeto) {
        String aux;
        Congreso congreso = (Congreso) objeto;
        int auxIdEstado = Facade.obtenerIdFijo(congreso.getEstado(), 0);
        aux = "Id_Estado = '" + auxIdEstado + "'," + "Nombre = '" + congreso.getNombreCong() + "'," + "Cantidad_DVD = '" + congreso.getCantDvd() + "'," + "Fecha_Fin = '" + congreso.getFechaFin() + "'," + "stock = '" + congreso.getStockDvd() + "'," + "malos = '" + congreso.getCantMalos() + "'," + "boxes = '" + congreso.getCantBoxes() + "'," + "boxesMalos ='" + congreso.getCantBoxesMalos() + "'";
        return aux;
    }

    protected String ides_u(Object objeto) {
        String aux;
        Congreso congreso = (Congreso) objeto;
        aux = " Iniciales='" + congreso.getIniciales() + "'";
        return aux;
    }

    protected String table_d() {
        return "null";
    }

    protected String campos_d(Object objeto) {
        return "null";
    }
}

package SOAPVO;

import orm.SA_Cargo;

public class CargoSOAPVO {

    String id_cargo;

    String descripcion;

    public static CargoSOAPVO crearCargoSOAPVO(SA_Cargo cargoOrm) {
        CargoSOAPVO obj = new CargoSOAPVO();
        obj.setId_cargo(cargoOrm.getId_cargo());
        obj.setDescripcion(cargoOrm.getDescripcion());
        return obj;
    }

    public String getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(String id_cargo) {
        this.id_cargo = id_cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package org.digitall.common.resourcesrequests.classes;

import org.digitall.common.cashflow.classes.Voucher;
import org.digitall.lib.sql.LibSQL;

public class ResourcesMovementDetail {

    private int idmovementdetail = -1;

    private String estado = "";

    private ResourceMovements resourceMovements;

    private Voucher voucher;

    public ResourcesMovementDetail() {
    }

    public void setIdmovementdetail(int _idmovementdetail) {
        idmovementdetail = _idmovementdetail;
    }

    public int getIdmovementdetail() {
        return idmovementdetail;
    }

    public void setEstado(String _estado) {
        estado = _estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setResourceMovements(ResourceMovements _resourceMovements) {
        resourceMovements = _resourceMovements;
    }

    public ResourceMovements getResourceMovements() {
        return resourceMovements;
    }

    public void setVoucher(Voucher _voucher) {
        voucher = _voucher;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void retrieveData() {
    }

    public int saveData() {
        int result = -1;
        String params = "";
        if (idmovementdetail == -1) {
            params = "" + resourceMovements.getIdmovement() + "," + voucher.getIdVoucher();
            result = LibSQL.getInt("resourcescontrol.addResourcesMovementDetail", params);
            idmovementdetail = result;
        } else {
            result = -1;
        }
        return result;
    }
}

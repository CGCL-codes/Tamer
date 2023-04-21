package com.jpress.control;

import com.jpress.dao.DAOAdmin;
import com.jpress.model.Admin;

public class AdminControl {

    DAOAdmin daoAdmin = new DAOAdmin();

    public int getAdminQtde() {
        return (int) this.daoAdmin.findAll().size();
    }

    public boolean cadastrarAdmin(Admin novoAdmin) {
        try {
            this.daoAdmin.begin();
            this.daoAdmin.persist((Admin) novoAdmin);
            this.daoAdmin.commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            this.daoAdmin.close();
        }
    }

    public boolean atualizarAdmin(Admin adminAtual) {
        try {
            this.daoAdmin.begin();
            this.daoAdmin.merge(adminAtual);
            this.daoAdmin.commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            this.daoAdmin.close();
        }
    }

    public boolean verificarEmailCadastrado(String email) {
        if (this.daoAdmin.findByEmail(email) != null) {
            return false;
        }
        return true;
    }
}

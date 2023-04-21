package com.pegaprecos.action;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Action da tela FaleConosco.
 * 
 * @author PegaPre�os
 * 
 */
public class FaleConoscoAction extends GenericLateralAction {

    public String carregar() throws Exception {
        return super.carregar();
    }

    public String salvar() throws Exception {
        try {
            Context ctx = new InitialContext();
            String emailSmtpServer = (String) ctx.lookup("java:comp/env/emailSmtpServer");
            String emailUserName = (String) ctx.lookup("java:comp/env/emailUserName");
            String emailPassword = (String) ctx.lookup("java:comp/env/emailPassword");
            SimpleEmail email = new SimpleEmail();
            email.setHostName(emailSmtpServer);
            email.setAuthentication(emailUserName, emailPassword);
            email.addTo("pegaprecos@pegaprecos.com.br", "PegaPrecos.com.br");
            email.setFrom(getEmail(), getNome());
            email.setSubject("Mensagem de " + getNome() + " enviada a partir do site PegaPrecos.com.br");
            email.setMsg(getMensagem() + (!getTelefone().equals("") ? "\nTelefone de contato:" + getTelefone() : ""));
            email.send();
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        } catch (EmailException e) {
            throw new Exception(e.getMessage());
        }
        return SUCCESS;
    }

    private String nome;

    private String email;

    private String telefone;

    private String mensagem;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

package com.unmsm.fisi.clinica.ws.dominio.modelo;

public class User {

    private Long id;

    private String userId;

    private String password;

    private Profile profile;

    private Patient patient;

    private Doctor doctor;

    public User(String userId, String password) {
        super();
        this.userId = userId;
        this.password = password;
    }

    public User(Long id, String userId, String password) {
        super();
        this.id = id;
        this.userId = userId;
        this.password = password;
    }

    public User(String userId, String password, Profile profile) {
        super();
        this.userId = userId;
        this.password = password;
        this.profile = profile;
    }

    public User(Long id, String userId, String password, Profile profile) {
        super();
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.profile = profile;
    }

    public Long id() {
        return this.id;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public String userId() {
        return this.userId;
    }

    public void changeUserId(String userId) {
        this.userId = userId;
    }

    public String password() {
        return this.password;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public Profile profile() {
        return this.profile;
    }

    public void changeProfile(Profile profile) {
        this.profile = profile;
    }
}

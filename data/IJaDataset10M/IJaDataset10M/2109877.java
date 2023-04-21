package com.mclub.server.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.mclub.client.enums.FilmStatus;
import com.mclub.client.model.ChangeStatusDetailDTO;

/**
 * Aciklamalar icin client tarafinda ayni isimli 
 * sinifin javadoclarina bakiniz
 * 
 * @see ChangeStatusDetailDTO
 * 
 * @author mdpinar
 * 
 */
@Entity
@Table
public class ChangeStatusDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long transId;

    @ManyToOne
    private ChangeStatus changeStatus;

    @ManyToOne
    private Movie movie;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private FilmStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public ChangeStatus getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(ChangeStatus changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public FilmStatus getStatus() {
        return status;
    }

    public void setStatus(FilmStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChangeStatusDetail) {
            ChangeStatusDetail model = (ChangeStatusDetail) obj;
            if (model.getId() != null && model.getId().equals(this.id)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}

package movierental.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_PenyewaanDetail")
public class PenyewaanDetail {

    @Id
    @GeneratedValue
    @Column(name = "pdetail_id")
    private int pDetailId;

    @ManyToOne
    @JoinColumn(name = "kode_film")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "nomor_faktur")
    private PenyewaanHeader penyewaanHeader;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getpDetailId() {
        return pDetailId;
    }

    public void setpDetailId(int pDetailId) {
        this.pDetailId = pDetailId;
    }

    public PenyewaanHeader getPenyewaanHeader() {
        return penyewaanHeader;
    }

    public void setPenyewaanHeader(PenyewaanHeader penyewaanHeader) {
        this.penyewaanHeader = penyewaanHeader;
    }

    @Override
    public String toString() {
        return "PenyewaanDetail [pDetailId=" + pDetailId + ", Movie=" + movie + ", PenyewaanHeader=" + penyewaanHeader + "]";
    }
}

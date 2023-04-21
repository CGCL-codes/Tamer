package org.itx.equipment.l1;

import java.util.List;
import javax.ejb.Remote;
import org.itx.equipment.l0.EQJuridical;

@Remote
public abstract interface Juridicals {

    public abstract void rem(Long paramLong);

    public abstract void rem(EQJuridical paramEQJuridical);

    public abstract void add(EQJuridical paramEQJuridical) throws EQException;

    public abstract void update(EQJuridical paramEQJuridical) throws EQException;

    public abstract Long getSizeAll();

    public abstract Long getSizeAll(String paramString);

    public abstract Long getSizeDeleted();

    public abstract Long getSizeDeleted(String paramString);

    public abstract List<EQJuridical> getAll();

    public abstract List<EQJuridical> getAll(String paramString);

    public abstract List<EQJuridical> getAll(Long paramLong1, Long paramLong2);

    public abstract List<EQJuridical> getAll(String paramString, Long paramLong1, Long paramLong2);

    public abstract List<EQJuridical> getDeleted();

    public abstract List<EQJuridical> getDeleted(String paramString);

    public abstract List<EQJuridical> getDeleted(Long paramLong1, Long paramLong2);

    public abstract List<EQJuridical> getDeleted(String paramString, Long paramLong1, Long paramLong2);

    public abstract List<EQJuridical> getHistory(Long paramLong);

    public abstract List<EQJuridical> getHistory(EQJuridical paramEQJuridical);

    public abstract EQJuridical get(Long paramLong);
}

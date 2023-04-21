package com.proyecto.bigbang.core.repository;

import org.testng.annotations.Test;
import com.proyecto.bigbang.core.application.SpringManager;
import com.proyecto.bigbang.core.domain.CicloLectivo;
import com.proyecto.bigbang.core.domain.Curso;
import com.proyecto.bigbang.core.domain.ProgramaCurricular;
import com.proyecto.bigbang.core.domain.Turno;

public class CursoRepositoryTest {

    @Test()
    public void crearCurso() {
        Curso c = new Curso();
        c.setTurno(new Turno(new Integer(1)));
        c.setCicloLectivo(new CicloLectivo(new Integer(1)));
        c.setProgramaCurricular(new ProgramaCurricular(new Integer(1)));
        ICursoRepository cr = (ICursoRepository) SpringManager.getBean("cursoRepository");
        cr.makePersistent(c);
    }

    @Test
    public void nuevoCurso() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.nuevoCurso();
    }

    @Test(enabled = false)
    public void asociarACurso() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.asociarACurso();
    }

    @Test
    public void asociarProfesorACurso() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.asociarProfACurso();
    }

    @Test
    public void quitarProfesorACurso() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.quitarProfesorMateria();
    }

    @Test(enabled = false)
    public void traerCursoTest() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.traerCurso();
    }

    @Test(enabled = false)
    public void horarioCurso() {
        PresenterTest presenter = (PresenterTest) SpringManager.getBean("presenterTest");
        presenter.horarioCurso();
    }
}

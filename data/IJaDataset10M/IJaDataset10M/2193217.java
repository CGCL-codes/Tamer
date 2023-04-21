package example.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import example.dao.TodoJdoDao;
import example.model.TodoJdo;

@Service
public class TodoJdoServiceImpl implements TodoJdoService {

    private TodoJdoDao todoJdoDao;

    @Autowired
    public void setTodoJdo(TodoJdoDao todoJdoDao) {
        this.todoJdoDao = todoJdoDao;
    }

    @Override
    public void addTodoJdo(TodoJdo todoJdo) {
        this.todoJdoDao.create(todoJdo);
    }

    @Override
    public List<TodoJdo> findAllTodoJdos() {
        return this.todoJdoDao.findAll();
    }
}

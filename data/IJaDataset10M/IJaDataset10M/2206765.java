package name.vaccari.matteo.tai.chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.vaccari.matteo.tai.chat.domain.Message;
import name.vaccari.matteo.tai.chat.domain.MessageRepository;
import name.vaccari.matteo.tai.chat.persistence.DatabaseConnector;
import name.vaccari.matteo.tai.chat.persistence.DatabaseExecutor;
import name.vaccari.matteo.tai.chat.persistence.DatabaseMessageRepository;
import name.vaccari.matteo.tai.chat.views.ChatView;

public class ChatServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseConnector connector = new DatabaseConnector("tai_chat", "tai_chat", "jdbc:mysql://localhost/tai_chat", "com.mysql.jdbc.Driver");
        DatabaseExecutor executor = new DatabaseExecutor(connector);
        DatabaseMessageRepository repository = new DatabaseMessageRepository(executor);
        ChatView view = new ChatView();
        ChatController controller = new ChatController(repository, view);
        controller.service(request.getMethod(), new Message(request.getParameter("message")), response.getWriter());
    }
}

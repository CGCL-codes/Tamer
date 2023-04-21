package _FACTORY;

public interface SerialServerFactoryOperations {

    int create(String url_ref, int serial_id) throws OBJECTS.CreationFailed;

    int remove(int server_id);
}

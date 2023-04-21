package sobchak.event;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 05.03.2010
 * Time: 13:57:19
 * To change this template use File | Settings | File Templates.
 */
public class EventProducer {

    protected EventProcessor listener = EventProcessor.buildEventProcessor();

    public enum Events {

        ACTIVATE_ACTIVE_CEMENT_TANK("������� ������������ �������� ����� � ��������", "������� ������������ �������� ����� � ��������", true), CEMENT_TANK_TO_OLD("������ ������", "������ ������", true), CEMENT_TANK_NEW("����� ����� ������� ��������", "����� ����� ������� ��������", false), CEMENT_TANK_ACTIVATED("����� � ������� �������", "", false), CEMENT_TANK_INACTIVE("����� � ������� ���������", "", false), BAD_CEMENT_BRICK("������� ��� ������ �� ������", "������� ��� ������ �� ������", true), BRICK_CEMENT_USED("������� ��� ������ ��� � �����", "������� ��� ������ ��� � �����", true), NEW_BRICK("����� �������", "", true), NEW_CEMENT_PORTION("����� ������ �������", "", false), WRONG_TAJIK("����������� ������", "����������� ������", true), BRICK_PUT("������� �������", "", true), CEMENT_BUILDER_UKNNOWN_TAJIK("����������� ������ ����� ������", "����������� ������ ����� ������", true), CEMENT_TANK_CONCURRENT_ACCESS("��� ������� ������� � ����� � ��������", "��� ������� ������� � ����� � ��������", true), NEW_TAJIK("������� ���������� � �������", "������� ���������� � �������", false), HIT_TAJIK("������� �������", "", false);

        String info_text;

        String exception_text;

        boolean need_throw_exception;

        Events(String info_text, String exception_text, boolean need_throw_exception) {
            this.info_text = info_text;
            this.exception_text = exception_text;
            this.need_throw_exception = need_throw_exception;
        }
    }

    public String toString() {
        return "";
    }
}

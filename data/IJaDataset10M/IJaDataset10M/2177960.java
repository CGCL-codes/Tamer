package languages;

import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.Language;

/**
 * @author xavier
 */
public class Korean extends Language {

    public Korean() {
        super("kr");
        addSupportedLanguage("kr_KR");
        addTranslatedMessage(CustomMessageId.WELCOME_VIP, "VIP플레이어 %s님 반갑습니다.\nCopyright 2011 www.sejun.info..\n서버 배율:\n경험치: %d\n퀘스트: %d\n드랍: %d\n");
        addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "프리미엄플레이어 %s님 반갑습니다.\nCopyright 2011 www.sejun.info..\n서버 배율:\n경험치: %d\n퀘스트: %d\n드랍: %d\n");
        addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "%s님 반갑습니다.\nCopyright 2011 www.sejun.info..\n서버 배율:\n경험치: %d\n퀘스트: %d\n드랍: %d\n");
        addTranslatedMessage(CustomMessageId.SERVER_REVISION, "서버 버전정보: %-6s");
        addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "저희 %s 서버에 오신것을 환영합니다.\nCopyright 2011 www.sejun.info.");
        addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, "%s님이 접속하셨습니다..");
        addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "사용권한이 부족합니다. 명령어를 사용할 수 없습니다.");
        addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "플레이어 %s님은 접속중이 아닙니다.");
        addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "옵션값을 숫자로 입력해야 합니다.");
        addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "입력한 옵션값이 숫자로 변환이 불가능합니다.");
        addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "알 수 없는 오류 발생");
        addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "이 명령어는 사용 불가능합니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "사용법: //아이템추가 <플레이어이름> <아이템ID> <수량>");
        addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "%s에게 정상적으로 아이템을 생성하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "운영자 %s님으로부터 %d개의 아이템을 받았습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "%d는 존재하지 않는 아이템입니다. 템플릿을 확인하세요. %s에게 아이템 추가가 실패하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "사용법: //드랍추가 <NPCID> <아이템ID> <최소 수량> <최대 수량> <확률>");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "사용법: //셋트추가 <플레이어이름> <아이템셋트ID>\n아이템셋트ID는 item_sets 템플릿을 참고하세요.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "잘못된 아이템셋트ID입니다. 아이템셋트ID를 확인하세요.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "아이템 지급을 위한 인벤토리 공간이 %d칸 부족합니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "%d 아이템이 %s님에게 정상 지급되지 않았습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "아이템 셋트 %d가 %s님에게 정상 지급되었습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "운영자 %s님이 당신에게 아이템셋트를 지급하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "사용법: //스킬추가 <스킬번호> <스킬레벨>");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "스킬 %d를 %s님에게 정상적으로 추가하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "운영자에 의해 스킬 %s가 새롭게 추가되었습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "사용법: //타이틀추가 <타이틀번호> <플레이어이름> [종족무시]");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "타이틀번호 %d는 사용할 수 없습니다., 1~50까지만 사용하세요.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "타이틀번호 %d가 이미 존재하거나 종족이 달라 추가할 수 없습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "타이틀번호 %d는 %s에게 이미 존재하거나 종족이 달라 추가할 수 없습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "타이틀번호 %d 추가에 성공하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "타이틀번호 %d가 %s님에게 성공적으로 추가되었습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "운영자 %s님이 당신에게 타이틀번호 %d를 추가하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "사용법: //테스트패킷보내기 <파일이름>");
        addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "파일명 %s를 찾을 수 없습니다.\n./data/packets 위치를 확인하세요.");
        addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "패킷데이터가 비어있습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "%s채널에서 채팅이 불가능합니다, 채널 %s을 사용하시거나 %s 종족에 맞는 채널을 사용하세요.");
        addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "이 채널은 사용이 불가능합니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "이미 %s 채널로 지정되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "%s 채널로 지정되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "이 채널에서 대화할 수 없습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "%s와 %s 채널로 지정되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "%s로 채팅채널을 지정해제하였습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "지정해제할 수 없습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "해당 채널로 지정되어 있지 않습니다, %s 채널로 지정되어 있습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "%s 채널 지정해제 되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "%s와 %s 채널 지정해제되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "시간경과로 채팅채널에서 채팅금지가 해제되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "%s님이 시간경과로 채팅채널에서 채팅금지가 해제되었습니다.");
        addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ASMOS, "마족");
        addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ELYOS, "천족");
        addTranslatedMessage(CustomMessageId.CHANNEL_NAME_WORLD, "전체");
        addTranslatedMessage(CustomMessageId.CHANNEL_NAME_BOTH, "마족/천족");
        addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ASMOS, "마족");
        addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ELYOS, "천족");
        addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_WORLD, "전체");
        addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_BOTH, "천마족");
        addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "%s 채팅채널에서 %s 이유로 %s분동안 채팅금지되셨습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d개의 스킬과 %d개의 스티그마스킬을 추가하였습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d개의 스킬이 추가되었습니다.");
        addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "존재하지 않는 명령어입니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "경험치 획득이 중지되었습니다. \".경험치획득중지\" 명령어로 다시 가능하도록 할 수 있습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "이미 경험치 획득이 중지되어 있습니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "다시 경험치 획득이 가능합니다.");
        addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "이미 경험치 획득이 가능도록 되어 있습니다.");
    }
}
